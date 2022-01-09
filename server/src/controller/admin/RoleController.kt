/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.controller.admin

import net.mamoe.mirai.plugincenter.controller.ADMIN_BASE_URL
import net.mamoe.mirai.plugincenter.dto.*
import net.mamoe.mirai.plugincenter.model.PermissionEntity
import net.mamoe.mirai.plugincenter.services.RoleService
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import net.mamoe.mirai.plugincenter.utils.permissions
import net.mamoe.mirai.plugincenter.utils.validate.requires
import net.mamoe.mirai.plugincenter.utils.withExchange
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("$ADMIN_BASE_URL/role")
class RoleController(
    val roleSvc: RoleService
) {
    @GetMapping("list")
    fun roles(): ApiResp<List<Any>> {
        return ApiResp.ok(roleSvc.roles.map(RoleData.Companion::fromRole))
    }

    @PostMapping("create")
    fun createRole(
        @RequestBody
        request: CreateRoleRequest,
        @ApiIgnore exchange: ServerWebExchange): ApiResp<CreateRoleResponse> {
        withExchange(exchange) {
            requires {
                loginUser can PermissionEntity.WriteRoleList
            }
        }

        val user = exchange.loginUserOrReject

        val role = roleSvc.createRole(user, request.roleName)

        return ApiResp.ok(CreateRoleResponse(role.id))
    }

    @PostMapping("assign")
    fun assignPermission(
        @RequestBody
        request: ModifyPermissionRequest,

        @ApiIgnore exchange: ServerWebExchange): ApiResp<Nothing?> {
        withExchange(exchange) {
            requires {
                loginUser can PermissionEntity.WriteRolePermissionList
            }
        }

        val user = exchange.loginUserOrReject

        val roleById = roleSvc.findRoleById(request.roleId)
            ?: throw IllegalArgumentException("role with id '${request.roleId}' doesn't exist.")

        val permissionByCode = permissions[request.permissionCode]
            ?: throw IllegalArgumentException("permission with code '${request.permissionCode}' doesn't exist.")

        // do assign
        with (roleSvc) {
            roleById.assignPermission(user, permissionByCode)
        }

        return ApiResp.ok()
    }

    @PostMapping("deassign")
    fun dropPermission(
        @RequestBody
        request: ModifyPermissionRequest,

        @ApiIgnore
        exchange: ServerWebExchange): ApiResp<Nothing?> {
        withExchange(exchange) {
            requires {
                loginUser can PermissionEntity.WriteRolePermissionList
            }
        }

        val user = exchange.loginUserOrReject

        return roleSvc.withExistRole(request.roleId) { role ->
            roleSvc.withExistPermission(request.permissionCode) { permission ->
                with (roleSvc) {
                    role.dropPermission(user, permission)

                    ApiResp.ok()
                }
            }
        }
    }

    @PostMapping("delete")
    fun deleteRole(
        @RequestBody
        request: DeleteRoleRequest,

        @ApiIgnore
        exchange: ServerWebExchange
    ): ApiResp<Nothing?> {
        withExchange(exchange) {
            requires {
                loginUser can PermissionEntity.WriteRoleList
            }
        }

        val user = exchange.loginUserOrReject

        roleSvc.deleteRole(user, request.roleId, request.force)

        return ApiResp.ok()
    }
}