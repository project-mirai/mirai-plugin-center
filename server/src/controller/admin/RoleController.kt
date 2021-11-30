/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.controller.admin

import net.mamoe.mirai.plugincenter.dto.ApiResp
import net.mamoe.mirai.plugincenter.dto.AssignPermissionRequest
import net.mamoe.mirai.plugincenter.dto.CreateRoleRequest
import net.mamoe.mirai.plugincenter.dto.RoleData
import net.mamoe.mirai.plugincenter.model.RoleEntity
import net.mamoe.mirai.plugincenter.services.RoleService
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import net.mamoe.mirai.plugincenter.utils.permissions
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/admin/role")
class RoleController(
    val roleSvc: RoleService
) {
    @GetMapping("list")
    fun roles(): ApiResp<List<Any>> {
        return ApiResp.ok(roleSvc.roles.map(RoleData.Companion::fromRole))
    }

    @PostMapping("create")
    fun newRole(
        @RequestBody
        request: CreateRoleRequest,
        @ApiIgnore exchange: ServerWebExchange): ApiResp<Nothing?> {
        // TODO: check permission

        val user = exchange.loginUserOrReject

        roleSvc.newRole(user, request.name)

        return ApiResp.ok()
    }

    @PostMapping("assign")
    fun assignPermission(
        @RequestBody
        request: AssignPermissionRequest,

        @ApiIgnore exchange: ServerWebExchange): ApiResp<Nothing?> {
        // TODO: check permission

        val user = exchange.loginUserOrReject

        val roleByName = roleSvc.findRoleByName(request.roleName)
            ?: throw IllegalArgumentException("role with name '${request.roleName}' doesn't exist.")

        val permissionByCode = permissions[request.permissionCode]
            ?: throw IllegalArgumentException("permission with code '${request.permissionCode}' doesn't exist.")

        // do assign
        with (roleSvc) {
            roleByName.assignPermission(user, permissionByCode)
        }

        return ApiResp.ok()
    }
}