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
import net.mamoe.mirai.plugincenter.dto.PluginDesc
import net.mamoe.mirai.plugincenter.dto.toDto
import net.mamoe.mirai.plugincenter.repo.PluginRepo
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/plugins")
@Api
class PluginsController(
    val repo: PluginRepo
) {
    @GetMapping("/{id}")
    fun get(@PathVariable id: Int): ApiResp<PluginDesc> {
        val plugin = repo.findPluginEntityById(id)
        return ApiResp.success(plugin.toDto())
    }

    @GetMapping("/list/{page}")
    fun list(@PathVariable(required = false) page: Int = 0): ApiResp<List<PluginDesc>> {
        require(page >= 0) { "Page invalid: '$page'. Should be at least 0." }
        val plugin = repo.findPluginEntitiesByIdBetween(page * 20 + 1, (page + 1) * 20)
        return ApiResp.success(plugin.map { it.toDto() })
    }
}