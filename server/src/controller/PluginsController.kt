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
import kotlinx.coroutines.reactor.mono
import net.mamoe.mirai.plugincenter.advice.ExceptionResponse
import net.mamoe.mirai.plugincenter.dto.*
import net.mamoe.mirai.plugincenter.model.PluginEntity
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.repo.toStringGitLike
import net.mamoe.mirai.plugincenter.services.PluginDescService
import net.mamoe.mirai.plugincenter.services.PluginStorageService
import net.mamoe.mirai.plugincenter.services.updateOrDefault
import net.mamoe.mirai.plugincenter.utils.isAvailable
import net.mamoe.mirai.plugincenter.utils.isOwnedBy
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import org.springframework.core.annotation.Order
import org.springframework.core.io.FileSystemResource
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import springfox.documentation.annotations.ApiIgnore
import java.sql.Timestamp


@RestController
@RequestMapping("/v1/plugins")
@Api(tags = ["插件服务"], position = 2)
class PluginsController(
    val desc: PluginDescService,
    val storage: PluginStorageService,
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
        return r.ok(desc.getList(page).map { it.toDto() })
    }

    ///////////////////////////////////////////////////////////////////////////
    // Desc
    ///////////////////////////////////////////////////////////////////////////

    @ApiOperation("获取插件信息")
    @Order(1)
    @GetMapping("/{id}")
    fun get(
        @ApiParam("插件 ID", example = PluginDesc.ID_EXAMPLE)
        @PathVariable
        id: String,
    ): ApiResp<PluginDesc?> {
        val plugin = desc.get(id) ?: return r.notFound(null)
        return r.ok(plugin.toDto())
    }


    @Order(2)
    @ApiOperation("创建新插件或更新插件信息")
    @ApiResponses(
        ApiResponse(code = 409, message = "Id conflicted with an existing plugin owned by {plugin.owner}", response = ApiResp::class),
    )
    @RequestMapping("/{id}", method = [RequestMethod.PUT, RequestMethod.PATCH])
    fun (@receiver:ApiIgnore ServerWebExchange).put(
        @ApiParam("插件 ID", example = PluginDesc.ID_EXAMPLE)
        @PathVariable
        id: String,

        @RequestBody
        desc: PluginDescUpdate,
    ): ApiResp<Void?> {
        val user = loginUserOrReject

        val plugin = this@PluginsController.desc.get(id)
        if (plugin == null && request.method == HttpMethod.PATCH) {
            return r.notFound(message = "Id $id not found. Use method PUT to create a new plugin.")
        }
        if (plugin?.isOwnedBy(user) == false) {
            return r(null, HttpStatus.CONFLICT, "Id conflicted with an existing plugin owned by ${plugin.userByOwner.toStringGitLike()}")
        }

        this@PluginsController.desc.updateOrDefault(id, PluginEntity()) {
            pluginId = id
            desc.info?.let { info = it }
            desc.name?.let { name = it }
            userByOwner = user
            status = 0
            updateTime = Timestamp(System.currentTimeMillis())
        }

        return if (plugin == null) r(HttpStatus.CREATED)
        else r.ok()
    }


    @Order(3)
    @ApiOperation("删除插件")
    @DeleteMapping("/{id}")
    @ApiResponses(
        ApiResponse(code = 403, message = "Plugin is not owned by you", response = ApiResp::class),
    )
    fun (@receiver:ApiIgnore ServerWebExchange).delete(
        @ApiParam("插件 ID", example = PluginDesc.ID_EXAMPLE)
        @PathVariable
        id: String,
    ): ApiResp<Void?> {
        val user = loginUserOrReject
        val plugin = desc.get(id) ?: return r.notFound(null)
        plugin.checkOwnedBy(user)
        desc.delete(plugin.pluginId)
        return r.ok()
    }
    private fun PluginEntity.checkAvailable() {
        if (! isAvailable()) throw ExceptionResponse(HttpStatus.FORBIDDEN, "Plugin is not available")
    }

    private fun PluginEntity.checkOwnedBy(user: UserEntity) {
        if (!isOwnedBy(user)) throw ExceptionResponse(HttpStatus.FORBIDDEN, "Plugin is not owned by you")
    }

    ///////////////////////////////////////////////////////////////////////////
    // Versions
    ///////////////////////////////////////////////////////////////////////////

    @ApiOperation("下载一个文件")
    @GetMapping("/{id}/{version}/{filename}")
    @ApiResponses(
        ApiResponse(code = 404, message = "File not found.", response = ApiResp::class),
    )
    fun getFile(
        @ApiParam("插件 ID", example = PluginDesc.ID_EXAMPLE)
        @PathVariable
        id: String,

        @ApiParam("插件版本号")
        @PathVariable
        version: String,

        @ApiParam("文件名")
        @PathVariable
        filename: String,
    ): FileSystemResource {
        // This should be handled by the upstream Nginx server.
        //  Spring sed as a fallback.

        val resource = storage.get(id, version, filename)
        if (!resource.exists()) throw ExceptionResponse(404, "File not found")
        return resource
    }

    @ApiOperation("上传一个文件")
    @PutMapping("/{id}/{version}/{filename}")
    @ApiResponses(
        ApiResponse(code = 404, message = "Plugin not found", response = ApiResp::class),
        ApiResponse(code = 403, message = "Plugin is not owned by you", response = ApiResp::class),
        ApiResponse(code = 201, message = "Uploaded", response = ApiResp::class),
    )
    fun (@receiver:ApiIgnore ServerWebExchange).putFile(
        @ApiParam("插件 ID", example = PluginDesc.ID_EXAMPLE)
        @PathVariable
        id: String,

        @ApiParam("插件版本号")
        @PathVariable
        version: String,

        @ApiParam("文件名")
        @PathVariable
        filename: String,
    ): Mono<ApiResp<Void?>> = mono {
        val user = loginUserOrReject
        val plugin = desc.get(id) ?: return@mono r.notFound(null)

        plugin.checkOwnedBy(user)
        plugin.checkAvailable()

        if (!storage.hasVersion(plugin.pluginId, version)) return@mono r.notFound(message = "Version not found")
        val file = storage.get(plugin.pluginId, version, filename)
        if (file.exists()) return@mono r.conflict(null, message = "File already exists")

        storage.write(plugin.pluginId, version, filename, request.body)
        r.created()
    }

    @ApiOperation("删除一个版本及该版本下的所有文件")
    @DeleteMapping("/{id}/{version}")
    @ApiResponses(
        ApiResponse(code = 403, message = "Plugin is not owned by you", response = ApiResp::class),
        ApiResponse(code = 404, message = "Version not found", response = ApiResp::class),
    )
    fun (@receiver:ApiIgnore ServerWebExchange).deleteVersion(
        @ApiParam("插件 ID", example = PluginDesc.ID_EXAMPLE)
        @PathVariable
        id: String,

        @ApiParam("插件版本号")
        @PathVariable
        version: String,
    ): ApiResp<Void?> {
        val user = loginUserOrReject
        val plugin = desc.get(id) ?: return r.notFound(null)
        plugin.checkOwnedBy(user)
        if (!storage.hasVersion(plugin.pluginId, version)) return r.notFound(message = "Version not found")
        if (!storage.delete(plugin.pluginId, version)) {
            return r(500, message = "Failed to delete files due to internal error")
        }
        return r.ok()
    }
}
