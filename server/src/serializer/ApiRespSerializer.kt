/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.serializer

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import net.mamoe.mirai.plugincenter.dto.ApiResp
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class ApiRespSerializer(
    val fastResponse: Boolean = true
) : StdSerializer<ApiResp>(ApiResp::class.java) {
    override fun serialize(
        value: ApiResp,
        gen: JsonGenerator,
        provider: SerializerProvider
    ) {
        if (fastResponse && value is ApiResp.Serialized) {
            gen.writeRaw(value.rawString)
            return
        }
        gen.writeStartObject()
        gen.writeNumberField("code", value.code)
        value.message?.let { gen.writeStringField("message", it) }
        value.response?.let {
            gen.writeFieldName("response")
            provider.defaultSerializeValue(it, gen)
        }
        gen.writeEndObject()
    }
}