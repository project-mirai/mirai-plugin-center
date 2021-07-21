/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.controller

import net.mamoe.mirai.plugincenter.dto.ApiResp
import net.mamoe.mirai.plugincenter.dto.SetStateDto
import net.mamoe.mirai.plugincenter.dto.r
import net.mamoe.mirai.plugincenter.repo.PluginRepo
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange

@RestController("/admin")
class AdminController(private val pluginRepo: PluginRepo) {

    @PatchMapping("setstate")
    fun setPluginState(@RequestBody setStateDto: SetStateDto, ctx: ServerWebExchange): ApiResp<*> {
        val user = ctx.loginUserOrReject
        if (user.role != 1) {
            return r<Any>(HttpStatus.FORBIDDEN, "你不是管理员")
        }
        val plugin = pluginRepo.findByPluginId(setStateDto.pluginId) ?: return r.notFound("插件不存在")
        plugin.status = setStateDto.state
        pluginRepo.save(plugin)
        return r.ok("成功")
    }
}
