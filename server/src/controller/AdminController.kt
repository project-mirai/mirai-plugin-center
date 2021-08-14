/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.controller

import io.swagger.annotations.Api
import net.mamoe.mirai.plugincenter.dto.ApiResp
import net.mamoe.mirai.plugincenter.dto.SetStateDto
import net.mamoe.mirai.plugincenter.dto.r
import net.mamoe.mirai.plugincenter.event.PluginModifiedEvent
import net.mamoe.mirai.plugincenter.repo.PluginRepo
import net.mamoe.mirai.plugincenter.services.LogService
import net.mamoe.mirai.plugincenter.services.PluginDescService
import net.mamoe.mirai.plugincenter.utils.isAdmin
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import net.mamoe.mirai.plugincenter.utils.state
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController       // FIXME: /v1/admin
@RequestMapping("/admin")
@Api(tags = ["管理员服务"], position = 3)
class AdminController(
    private val pluginRepo: PluginRepo,
    private val desc: PluginDescService,
    private val logger: LogService,
) {

    @PatchMapping("/setstate")       // FIXME: setState ?
    fun setPluginState(@RequestBody setStateDto: SetStateDto, ctx: ServerWebExchange): ApiResp<*> {
        val user = ctx.loginUserOrReject
        if (! user.isAdmin) {
            return r<Any>(HttpStatus.FORBIDDEN, "你不是管理员")
        }

        val plugin = pluginRepo.findByPluginId(setStateDto.pluginId) ?: return r.notFound("插件不存在")

        desc.update(plugin) {
            this.state = setStateDto.state
        }

        return r.ok("成功")
    }
}
