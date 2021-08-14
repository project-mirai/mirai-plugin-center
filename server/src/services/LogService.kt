/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.services

import com.fasterxml.jackson.core.type.TypeReference
import net.mamoe.mirai.plugincenter.model.LogEntity
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.repo.LogRepo
import net.mamoe.mirai.plugincenter.utils.jsonMapper
import org.springframework.stereotype.Component
import java.lang.reflect.Type
import kotlin.reflect.KClass

@Component
class LogService(
    val repo: LogRepo
) {
    fun <T: Any> push(operator: UserEntity, detail: T, clazz: KClass<T>): LogEntity {
        val bytes = jsonMapper.writeValueAsBytes(detail)
        val map = jsonMapper.readValue(bytes, object : TypeReference<Map<String, Any>>() {
            override fun getType(): Type {
                return Map::class.java
            }
        })

        return repo.save(LogEntity().apply {
            this.operator = operator.uid
            this.msg = clazz.simpleName
            this.otherInfo = map
        })
    }
}