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
import java.sql.Timestamp
import kotlin.reflect.KClass

/**
 * # Log Service
 */
@Component
class LogService(
    val repo: LogRepo
) {
    /**
     * 创建一条根 log 条目
     *
     * @see LogService.newLog
     */
    fun <T: Any> newRootLog(operator: UserEntity, detail: T, clazz: KClass<T>): LogEntity {
        return newLog(null, operator, detail, clazz)
    }

    /**
     * 创建一条 log 条目
     *
     * @param parent 父 log，如果为 null 则是根 log
     * @param operator 进行操作的用户
     * @param detail 日志额外内容
     * @param clazz T 的 [KClass] 实例
     * @return 创建成功的 [LogEntity] 示例
     */
    fun <T: Any> newLog(parent: LogEntity?, operator: UserEntity, detail: T, clazz: KClass<T>): LogEntity {
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
            this.parent = parent
            this.logTime = Timestamp(System.currentTimeMillis())
        })
    }
}