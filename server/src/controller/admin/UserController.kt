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
import net.mamoe.mirai.plugincenter.dto.ModifyRoleRequest
import net.mamoe.mirai.plugincenter.services.RoleService
import net.mamoe.mirai.plugincenter.services.UserService
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/admin/user")
class UserController(
    val userSvc: UserService,
    val roleSvc: RoleService
) {

    @PostMapping("assign")
    fun assignRole(
        @RequestBody
        request: ModifyRoleRequest,

        @ApiIgnore
        exchange: ServerWebExchange
    ): ApiResp<Nothing?> {
        // TODO: check permission

        val user = exchange.loginUserOrReject

        val userById = userSvc.findUserById(request.uid)
            ?: throw IllegalArgumentException("user with id '${request.uid}' doesn't exist.")

        val roleByName = roleSvc.findRoleByName(request.roleName)
            ?: throw IllegalArgumentException("role with name '${request.roleName} doesn't exist.'")

        with(userSvc) {
            userById.assignRole(user, roleByName)
        }

        return ApiResp.ok()
    }

    @PostMapping("deassign")
    fun dropRole(
        @RequestBody
        request: ModifyRoleRequest,

        @ApiIgnore
        exchange: ServerWebExchange
    ): ApiResp<Nothing?> {
        // TODO: check permission

        val user = exchange.loginUserOrReject

        return userSvc.withExistUser(request.uid) { targetUser ->
            roleSvc.withExistRole(request.roleName) { role ->
                with (userSvc) {
                    targetUser.dropRole(user, role)
                }
            }

            ApiResp.ok()
        }
    }
}