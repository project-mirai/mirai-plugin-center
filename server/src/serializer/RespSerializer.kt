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
import net.mamoe.mirai.plugincenter.dto.Resp
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class RespSerializer : StdSerializer<Resp>(Resp::class.java) {
    override fun serialize(
        value: Resp,
        gen: JsonGenerator,
        provider: SerializerProvider
    ) {
        value.writeTo(gen, provider)
    }
}