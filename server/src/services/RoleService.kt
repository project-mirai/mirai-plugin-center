/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.services

import net.mamoe.mirai.plugincenter.event.AssignPermissionEvent
import net.mamoe.mirai.plugincenter.event.NewRoleEvent
import net.mamoe.mirai.plugincenter.model.PermissionEntity
import net.mamoe.mirai.plugincenter.model.RoleEntity
import net.mamoe.mirai.plugincenter.model.RolePermissionEntity
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.repo.RolePermissionRepo
import net.mamoe.mirai.plugincenter.repo.RoleRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class RoleService {
    @Autowired
    lateinit var roleRepo: RoleRepo

    @Autowired
    lateinit var logSvc: LogService

    @Autowired
    lateinit var rolePermissionRepo: RolePermissionRepo

    val roles: Iterable<RoleEntity>
    get() {
        return roleRepo.findAll()
    }

    fun findRoleByName(name: String): RoleEntity? = roleRepo.findByName(name)

    fun hasRole(name: String): Boolean = roleRepo.findByName(name) != null

    /**
     * 创建一个新的 role
     *
     * @param operator 操作用户
     * @param name role 名称，应该是唯一的
     */
    fun newRole(operator: UserEntity, name: String): RoleEntity {
        // check role name
        roleRepo.findByName(name)?.run {
            throw IllegalArgumentException("role with name '$name' is already exist.")
        }

        val log = logSvc.newRootLog(operator, NewRoleEvent, NewRoleEvent::class)

        return roleRepo.save(RoleEntity().apply {
            this.name = name
            this.log = log
        })
    }

    fun assignPermission(operator: UserEntity, roleEntity: RoleEntity, permission: PermissionEntity): RolePermissionEntity {
        // check role-permission relationship
        rolePermissionRepo.findByRoleAndPermission(roleEntity, permission.code)?.run {
            throw IllegalArgumentException("role with name '${roleEntity.name}' has had permission with code '${permission.code}'.")
        }

        val log = logSvc.newLog(
            roleEntity.logChain,
            operator,
            AssignPermissionEvent(permission),
            AssignPermissionEvent::class)

        return rolePermissionRepo.save(RolePermissionEntity().apply {
            this.role = roleEntity
            this.permission = permission.code
        })
    }
}