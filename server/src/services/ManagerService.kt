/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.services

import net.mamoe.mirai.plugincenter.model.PluginEntity
import net.mamoe.mirai.plugincenter.repo.PluginRepo
import org.springframework.stereotype.Service

@Service
class ManagerService(
    private val pluginRepo: PluginRepo
) {
    fun modifyStatus(id: String, status: PluginEntity.Status): PluginEntity? {
        val plugin = pluginRepo.findPluginEntityByPluginId(id) ?: return null

        return pluginRepo.save(plugin.apply {
            this.status = status.ordinal
        })
    }
}