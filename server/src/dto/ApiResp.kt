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
import net.mamoe.mirai.plugincenter.PluginCenterApplication
import net.mamoe.mirai.plugincenter.utils.toJsonUseJackson
import org.springframework.http.HttpStatus
import springfox.documentation.annotations.ApiIgnore

inline fun resp(code: Int = 200, builderAction: RespBuilder.() -> Unit): Resp {
    return RespBuilder(code).apply(builderAction)
}

class RespBuilder(
    private val code: Int,
) : Resp {
    private val map = mutableMapOf<String, Any?>()
    private var msg: String? = null

    operator fun String.minus(any: Any?) {
        map[this] = any
    }

    fun msg(msg: String) {
        this.msg = msg
    }

    override fun writeTo(gen: JsonGenerator, provider: SerializerProvider) {
        ApiResp(code, message = msg ?: "success", response = map).writeTo(gen, provider)
    }
}


sealed interface Resp {
    fun writeTo(gen: JsonGenerator, provider: SerializerProvider)

    companion object {
        val OK = SerializedApiResp<Unit>(HttpStatus.OK)
        val BAD_REQUEST = SerializedApiResp<Unit>(HttpStatus.BAD_REQUEST)
        val UNAUTHORIZED = SerializedApiResp<Unit>(HttpStatus.UNAUTHORIZED)
        val FORBIDDEN = SerializedApiResp<Unit>(HttpStatus.FORBIDDEN)
        val NOT_FOUND = SerializedApiResp<Unit>(HttpStatus.NOT_FOUND)
        val METHOD_NOT_ALLOWED = SerializedApiResp<Unit>(HttpStatus.METHOD_NOT_ALLOWED)
        val REQUEST_TIMEOUT = SerializedApiResp<Unit>(HttpStatus.REQUEST_TIMEOUT)
        val INTERNAL_SERVER_ERROR = SerializedApiResp<Unit>(HttpStatus.INTERNAL_SERVER_ERROR)
        val NOT_IMPLEMENTED = SerializedApiResp<Unit>(HttpStatus.NOT_IMPLEMENTED)
        val SERVICE_UNAVAILABLE = SerializedApiResp<Unit>(HttpStatus.SERVICE_UNAVAILABLE)
    }
}

@ApiModel
open class ApiResp<T>(
    @ApiModelProperty("状态码") val code: Int,
    @ApiModelProperty("提示信息") val message: String? = null,
    @ApiModelProperty("返回数据") val response: T? = null,
    @ApiIgnore private val trace: Trace? = null,
) : Resp {
    companion object {
        private val localMapper = JsonMapper().apply {
            registerModule(SimpleModule().apply {
                addSerializer(ApiResp::class.java, ApiRespSerializer)
            })
        }

        fun <T> success(response: T, code: Int = 200, message: String? = "success"): ApiResp<T> {
            return ApiResp(code, message, response)
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
        if (PluginCenterApplication.SHOW_TRACE) {
            trace?.let { trace -> gen.writeStringField("trace", trace) }
        }
        gen.writeEndObject()
    }
}

class SerializedApiResp<T>(
    code: Int,
    message: String? = null,
    response: T? = null,
    trace: Trace? = null,
) : ApiResp<T>(code, message, response, trace), Resp {
    constructor(code: HttpStatus) : this(code.value(), code.reasonPhrase)

    private val rawString: String by lazy { ApiResp(code, message, response, trace).toJsonString() }
    override fun writeTo(gen: JsonGenerator, provider: SerializerProvider) = gen.writeRaw(rawString)
}


typealias Trace = String
typealias TraceElement = StackTraceElement