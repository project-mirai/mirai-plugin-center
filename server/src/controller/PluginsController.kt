/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.controller

import io.swagger.annotations.*
import net.mamoe.mirai.plugincenter.dto.*
import net.mamoe.mirai.plugincenter.model.PluginEntity
import net.mamoe.mirai.plugincenter.repo.PluginRepo
import net.mamoe.mirai.plugincenter.repo.toStringGitLike
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import springfox.documentation.annotations.ApiIgnore
import java.sql.Timestamp


@RestController
@RequestMapping("/v1/plugins")
@Api(tags = ["插件服务"], position = 2)
class PluginsController(
    val repo: PluginRepo
) {
    @ApiOperation("获取插件列表")
    @Order(0)
    @GetMapping("/")
    fun list(
        @ApiParam("页码", allowableValues = "range[0, infinity]", required = false, defaultValue = "0")
        @RequestParam("page", required = false)
        page0: Int? = 0,
    ): ApiResp<List<PluginDesc>> {
        val page = page0 ?: 0
        require(page >= 0) { "Page invalid: '$page'. Should be at least 0." }
        val plugin = repo.findPluginEntitiesByIdBetween(page * 20 + 1, (page + 1) * 20)
        return r.ok(plugin.map { it.toDto() })
    }


    @ApiOperation("获取插件信息")
    @Order(1)
    @GetMapping("/{id}")
    fun get(
        @ApiParam("插件 ID", example = PluginDesc.ID_EXAMPLE)
        @PathVariable
        id: String,
    ): ApiResp<PluginDesc?> {
        val plugin = repo.findPluginEntityByPluginId(id) ?: return r.notFound(null)
        return r.ok(plugin.toDto())
    }


    @Order(2)
    @ApiOperation("上传插件或更新插件信息")
    @ApiResponses(
        ApiResponse(code = 409, message = "Id conflicted with an existing plugin owned by {plugin.owner}", response = ApiResp::class),
    )
    @RequestMapping("/{id}", method = [RequestMethod.PUT, RequestMethod.PATCH])
    fun put(
        @ApiIgnore
        exchange: ServerWebExchange,

        @ApiParam("插件 ID", example = PluginDesc.ID_EXAMPLE)
        @PathVariable
        id: String,

        @RequestBody
        desc: PluginDescUpdate,
    ): ApiResp<Void?> {
        val user = exchange.loginUserOrReject

        val plugin = repo.findPluginEntityByPluginId(id)
        if (plugin == null && exchange.request.method == HttpMethod.PATCH) {
            return r.notFound(message = "Id $id not found. Use method PUT to create a new plugin.")
        }
        if (plugin != null && plugin.userByOwner.uid != user.uid) {
            return r(null, HttpStatus.CONFLICT, "Id conflicted with an existing plugin owned by ${plugin.userByOwner.toStringGitLike()}")
        }

        repo.save((plugin ?: PluginEntity()).apply {
            pluginId = id
            desc.info?.let { info = it }
            desc.name?.let { name = it }
            userByOwner = user
            updateTime = Timestamp(System.currentTimeMillis())
        })

        return if (plugin == null) r(HttpStatus.CREATED)
        else r.ok()
    }


    @Order(3)
    @ApiOperation("删除插件")
    @DeleteMapping("/{id}")
    @ApiResponses(
        ApiResponse(code = 403, message = "Plugin is not owned by you", response = ApiResp::class),
    )
    fun delete(
        @ApiIgnore
        exchange: ServerWebExchange,

        @ApiParam("插件 ID", example = PluginDesc.ID_EXAMPLE)
        @PathVariable
        id: String,
    ): ApiResp<Void?> {
        val user = exchange.loginUserOrReject
        val plugin = repo.findPluginEntityByPluginId(id) ?: return r.notFound(null)
        if (plugin.userByOwner.uid != user.uid) return r(HttpStatus.FORBIDDEN, "Plugin is not owned by you")
        repo.delete(plugin)
        return r.ok()
    }

}
