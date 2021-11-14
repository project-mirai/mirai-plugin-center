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
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import net.mamoe.mirai.plugincenter.model.PermissionEntity
import org.springframework.boot.jackson.JsonComponent

@JsonComponent
class PermissionSerializer : StdSerializer<PermissionEntity>(PermissionEntity::class.java) {
    override fun serialize(entity: PermissionEntity?, gen: JsonGenerator, p2: SerializerProvider?) {
        if (entity != null) {
            gen.writeNumber(entity.code)
        } else {
            gen.writeNull()
        }
    }
}

@JsonComponent
class PermissionDeserializer : StdDeserializer<PermissionEntity>(PermissionEntity::class.java) {
    override fun deserialize(parser: JsonParser, ctx: DeserializationContext): PermissionEntity {
        val number = parser.numberValue

        return PermissionEntity.values().firstOrNull { it.code == number.toInt() }
            ?: ctx.handleWeirdNumberValue(PermissionEntity::class.java, number, "invalid permission code") as PermissionEntity
    }
}