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
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import net.mamoe.mirai.plugincenter.dto.*
import net.mamoe.mirai.plugincenter.model.PluginEntity
import net.mamoe.mirai.plugincenter.repo.PluginRepo
import net.mamoe.mirai.plugincenter.repo.toStringGitLike
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.sql.Timestamp


@RestController
@RequestMapping("/v1/plugins")
@Api
class PluginsController(
    val repo: PluginRepo
) {
    @Order(0)
    @GetMapping("/")
    fun list(
        @RequestParam("page", required = false) page0: Int? = 0
    ): ApiResp<List<PluginDesc>> {
        val page = page0 ?: 0
        require(page >= 0) { "Page invalid: '$page'. Should be at least 0." }
        val plugin = repo.findPluginEntitiesByIdBetween(page * 20 + 1, (page + 1) * 20)
        return r.ok(plugin.map { it.toDto() })
    }

    @Order(1)
    @GetMapping("/{id}")
    fun get(
        @PathVariable id: String,
    ): ApiResp<PluginDesc?> {
        val plugin = repo.findPluginEntityByPluginId(id) ?: return r.notFound(null)
        return r.ok(plugin.toDto())
    }

    @DeleteMapping("/{id}")
    fun delete(
        exchange: ServerWebExchange,
        @PathVariable id: String,
    ): ApiResp<Void?> {
        val user = exchange.loginUserOrReject
        val plugin = repo.findPluginEntityByPluginId(id) ?: return r.notFound(null)
        if (plugin.userByOwner.uid != user.uid) return r(HttpStatus.FORBIDDEN, "Plugin is not owned by you")
        repo.delete(plugin)
        return r.ok()
    }

    @ApiResponses(
        ApiResponse(code = 200, message = "OK", response = ApiResp::class),
        ApiResponse(code = 409, message = "Id conflicted with an existing plugin owned by {plugin.owner.", response = ApiResp::class),
    )
    @PutMapping("/{id}")
    fun put(
        exchange: ServerWebExchange,
        @PathVariable id: String,
        @RequestBody desc: PluginDesc,
    ): ApiResp<Void?> {
        require(desc.pluginId == id) { "body.id must == path.id" }

        val user = exchange.loginUserOrReject

        val plugin = repo.findPluginEntityByPluginId(id)
        if (plugin != null && plugin.userByOwner.uid != user.uid) {
            return r(null, HttpStatus.CONFLICT, "Id conflicted with an existing plugin owned by ${plugin.userByOwner.toStringGitLike()}")
        }

        repo.save(PluginEntity().copyFrom(desc).apply {
            userByOwner = user
            updateTime = Timestamp(System.currentTimeMillis())
        })

        return if (plugin == null) r(HttpStatus.CREATED)
        else r.ok()
    }
}
