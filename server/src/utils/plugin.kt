/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.utils

import net.mamoe.mirai.plugincenter.model.PluginEntity
import net.mamoe.mirai.plugincenter.model.UserEntity

/**
 * 检查插件状态
 *
 * @see PluginEntity.Status
 */
fun PluginEntity.isAvailable(): Boolean {
    return this.rawState == PluginEntity.Status.Accepted.ordinal
}

/**
 * 检查插件所有者
 *
 * @see PluginEntity.userByOwner
 */
fun PluginEntity.isOwnedBy(user: UserEntity): Boolean {
    return this.userByOwner.uid == user.uid || user.isAdmin
}

var PluginEntity.state: PluginEntity.Status
    get() {
        return PluginEntity.Status.values()
            .getOrNull(this.rawState)
            ?: PluginEntity.Status.Undefined
    }

    set(value) {
        this.rawState = value.ordinal
    }