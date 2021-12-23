/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.dto

import kotlinx.serialization.Serializable
import net.mamoe.mirai.plugincenter.model.PermissionEntity
import net.mamoe.mirai.plugincenter.model.RoleEntity
import net.mamoe.mirai.plugincenter.utils.permissions

data class RoleData(
    val id: Int?,
    val name: String?,
    val permissions: List<PermissionEntity>
) {
    companion object {
        fun fromRole(role: RoleEntity): RoleData {
            return RoleData(
                id = role.id,
                name = role.name,
                permissions = role.permissionSet.map { permissions[it.permission] ?: throw IllegalArgumentException("invalid permission code: ${it.permission}") }
            )
        }
    }
}

data class CreateRoleRequest(
    val roleName: String
)

data class DeleteRoleRequest(
    val roleId: Int,
    val force: Boolean = false
)

data class ModifyPermissionRequest(
    val roleId: Int,
    val permissionCode: Int
)