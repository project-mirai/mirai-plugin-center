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
import net.mamoe.mirai.plugincenter.dto.RoleData
import net.mamoe.mirai.plugincenter.model.RoleEntity
import net.mamoe.mirai.plugincenter.services.RoleService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/admin/role")
class RoleController(
    val roleSvc: RoleService
) {
    @GetMapping("list")
    fun roles(): ApiResp<List<Any>> {
        return ApiResp.ok(roleSvc.roles.map(RoleData.Companion::fromRole))
    }
}