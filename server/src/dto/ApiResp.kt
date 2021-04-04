/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.dto

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import net.mamoe.mirai.plugincenter.utils.toJsonUseJackson

sealed interface Resp {
    fun writeTo(gen: JsonGenerator, provider: SerializerProvider)

    companion object {
        val NOT_AUTHED = SerializedApiResp<Unit>(403,"Not authed")
        val NOT_FOUND = SerializedApiResp<Unit>(404, "Resource not found")
    }
}

@ApiModel
open class ApiResp <T>(
    @ApiModelProperty("状态码")  val code: Int,
    @ApiModelProperty("提示信息") val message: String? = null,
    @ApiModelProperty("返回数据") val response: T? = null,
    val trace: List<String>? = null,
) : Resp {
    private companion object {
        val localMapper = JsonMapper().apply {
            registerModule(SimpleModule().apply {
                addSerializer(ApiResp::class.java, ApiRespSerializer)
            })
        }
    }

    private object ApiRespSerializer : StdSerializer<ApiResp<*>>(ApiResp::class.java) {
        override fun serialize(value: ApiResp<*>, gen: JsonGenerator, provider: SerializerProvider) = value.writeTo(gen, provider)
    }

    fun toJsonString(): String = this.toJsonUseJackson(localMapper)
    override fun writeTo(gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeStartObject()
        gen.writeNumberField("code", code)
        message?.let { gen.writeStringField("message", it) }
        response?.let {
            gen.writeFieldName("response")
            provider.defaultSerializeValue(it, gen)
        }
        trace?.let { gen.writeObjectField("trace", it) }
        gen.writeEndObject()
    }
}

class SerializedApiResp<T>(
    code: Int,
    message: String? = null,
    response: T? = null,
    trace: List<String>? = null,
) : ApiResp<T>(code, message, response, trace), Resp {
    private val rawString: String by lazy { ApiResp(code, message, response, trace).toJsonString() }
    override fun writeTo(gen: JsonGenerator, provider: SerializerProvider) = gen.writeRaw(rawString)
}
