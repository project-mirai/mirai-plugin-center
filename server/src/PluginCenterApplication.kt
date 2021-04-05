/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@EnableJpaAuditing
@SpringBootApplication
class PluginCenterApplication {
    companion object {
        val logger by lazy {
            LoggerFactory.getLogger("net.mamoe.mirai.plugincenter")
        }

        val DEBUGGING: Boolean by lazy { logger.isDebugEnabled }
        val SHOW_TRACE: Boolean by lazy { logger.isTraceEnabled }
    }
}

fun main(args: Array<String>) {
    runApplication<PluginCenterApplication>(*args)
}
