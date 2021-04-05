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
import org.springframework.core.annotation.Order
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/v1/plugins")
@Api
class PluginsController(
    val repo: PluginRepo
) {
    @Order(0)
    @GetMapping("/")
    fun list(@RequestParam("page", required = false) page0: Int? = 0): ApiResp<List<PluginDesc>> {
        val page = page0 ?: 0
        require(page >= 0) { "Page invalid: '$page'. Should be at least 0." }
        val plugin = repo.findPluginEntitiesByIdBetween(page * 20 + 1, (page + 1) * 20)
        return ApiResp.ok(plugin.map { it.toDto() })
    }

    @Order(1)
    @GetMapping("/{id}/")
    fun get(@PathVariable id: Int): ApiResp<PluginDesc?> {
        val plugin = repo.findPluginEntityById(id) ?: return ApiResp.notFound(null)
        return ApiResp.ok(plugin.toDto())
    }
}
