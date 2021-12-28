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
import net.mamoe.mirai.plugincenter.event.DeleteRoleEvent
import net.mamoe.mirai.plugincenter.event.DropPermissionEvent
import net.mamoe.mirai.plugincenter.event.NewRoleEvent
import net.mamoe.mirai.plugincenter.model.PermissionEntity
import net.mamoe.mirai.plugincenter.model.RoleEntity
import net.mamoe.mirai.plugincenter.model.RolePermissionEntity
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.repo.RolePermissionRepo
import net.mamoe.mirai.plugincenter.repo.RoleRepo
import net.mamoe.mirai.plugincenter.repo.UserRoleRepo
import net.mamoe.mirai.plugincenter.utils.permissions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val userRoleRepo: UserRoleRepo,
    private val rolePermissionRepo: RolePermissionRepo
) {
    @Autowired
    lateinit var roleRepo: RoleRepo

    @Autowired
    lateinit var logSvc: LogService

    val roles: Iterable<RoleEntity>
    get() {
        return roleRepo.findAll()
    }

    /// region CRUD Role
    fun findRoleByName(name: String): RoleEntity? = roleRepo.findByName(name)
    fun findRoleById(id: Int): RoleEntity? = roleRepo.findById(id).takeIf { it.isPresent }?.get()

    fun hasRole(name: String): Boolean = roleRepo.findByName(name) != null

    /// endregion

    /**
     * 创建一个新的 role
     *
     * @param operator 操作用户
     * @param name role 名称，应该是唯一的
     */
    fun createRole(operator: UserEntity, name: String): RoleEntity {
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

    /**
     * 为 role 赋予权限
     *
     * @receiver 目标 role
     * @param operator 操作者
     * @param permission 目标权限
     */
    fun RoleEntity.assignPermission(operator: UserEntity, permission: PermissionEntity): RolePermissionEntity {
        // check role-permission relationship
        rolePermissionRepo.findByRoleAndPermission(this, permission.code)?.run {
            throw IllegalArgumentException("role with name '${this@assignPermission.name}' has had permission with code '${permission.code}'.")
        }

        val log = logSvc.newLog(
            this.logChain,
            operator,
            AssignPermissionEvent(permission),
            AssignPermissionEvent::class)

        return rolePermissionRepo.save(RolePermissionEntity().apply {
            this.role = this@assignPermission
            this.permission = permission.code
        })
    }

    /**
     * 为 role 删除权限
     *
     * @receiver 目标 role
     * @param operator 操作者
     * @param permission 目标权限
     */
    fun RoleEntity.dropPermission(operator: UserEntity, permission: PermissionEntity): Unit {
        withExistRolePermit(this, permission) {
            val log = logSvc.newLog(
                this.logChain,
                operator,
                DropPermissionEvent(permission),
                DropPermissionEvent::class)

            rolePermissionRepo.delete(it)
        }
    }

    fun deleteRole(operator: UserEntity, roleId: Int, force: Boolean = false) {
        withExistRole(roleId) { role ->
            val userRole = userRoleRepo.findAllByRole(role)

            // inspecting the user-role relationship and reject or remove those
            if (userRole.any()) {
                if (force) {
                    // remove all user-role relationship of this role
                    userRoleRepo.deleteAll(userRole)
                } else {
                    // reject operation
                    val userList = userRole.joinToString(", ", limit = 3) { "(uid: ${it.user.uid}) ${it.user.nick}" }

                    throw IllegalArgumentException("role with id '$roleId' is already assigned to other user: $userList")
                }
            }

            // remove all role-permission relationships of this role
            with (rolePermissionRepo) {
                deleteAll(findAllByRole(role))
            }

            logSvc.newLog(
                role.logChain,
                operator,
                DeleteRoleEvent,
                DeleteRoleEvent::class
            )

            roleRepo.delete(role)
        }
    }

    final inline fun <R> withExistRole(roleId: Int, block: (RoleEntity) -> R): R {
        val role = findRoleById(roleId)
            ?: throw IllegalArgumentException("the role with id '${roleId}' doesn't exist")

        return block(role)
    }

    final inline fun <R> withoutExistRole(roleName: String, block: () -> R): R {
        findRoleByName(roleName)?.run {
            throw IllegalArgumentException("a role with name '$roleName' already exists")
        }

        return block()
    }

    final inline fun <R> withExistPermission(permissionCode: Int, block: (PermissionEntity) -> R): R {
        val permission = permissions[permissionCode] ?: throw IllegalArgumentException("the permission with code '$permissionCode' doesn't exist")

        return block(permission)
    }

    final inline fun <R> withExistRolePermit(role: RoleEntity, permissionCode: Int, block: (RolePermissionEntity) -> R): R {
        val relationship = role.permissionSet.find { it.permission == permissionCode }
            ?: throw IllegalArgumentException("the role with name '${role.name}' doesn't obtain a permission with code '$permissionCode'")

        return block(relationship)
    }

    final inline fun <R> withExistRolePermit(role: RoleEntity, permission: PermissionEntity, block: (RolePermissionEntity) -> R): R =
        withExistRolePermit(role, permission.code, block)

    final inline fun <R> withoutExistRolePermit(role: RoleEntity, permissionCode: Int, block: () -> R): R {
        role.permissionSet.find { it.permission == permissionCode }?.run {
            throw IllegalArgumentException("the role with name '${role.name}' already had the permission with code '$permissionCode'")
        }

        return block()
    }

    final inline fun <R> withoutExistPermit(role: RoleEntity, permission: PermissionEntity, block: () -> R): R =
        withoutExistRolePermit(role, permission.code, block)
}