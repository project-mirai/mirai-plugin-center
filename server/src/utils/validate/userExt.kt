/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.utils.validate

import net.mamoe.mirai.plugincenter.model.PermissionEntity
import net.mamoe.mirai.plugincenter.model.RoleEntity
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.utils.permissions

fun RoleEntity.hasPermit(predicate: (PermissionEntity) -> Boolean): Boolean {
    return this.permissionSet.asSequence()
        .map { permissions[it.permission] ?: throw InternalError("missing permission code: ${it.permission}") }
        .find(predicate) !== null
}

fun RoleEntity.hasPermit(permit: PermissionEntity): Boolean = hasPermit { it === permit }

fun UserEntity.hasRole(predicate: (RoleEntity) -> Boolean): Boolean {
    return this.rolesByUid.asSequence().map { it.role }.find(predicate) !== null
}

fun UserEntity.hasRole(roleId: Int): Boolean = hasRole { it.id == roleId }

fun UserEntity.hasPermit(predicate: (PermissionEntity) -> Boolean): Boolean =
    hasRole {
        it.hasPermit(predicate)
    }

fun UserEntity.hasPermit(permit: PermissionEntity): Boolean = hasPermit { it === permit }