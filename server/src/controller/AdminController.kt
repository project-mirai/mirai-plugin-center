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
import io.swagger.annotations.ApiOperation
import net.mamoe.mirai.plugincenter.advice.ExceptionResponse
import net.mamoe.mirai.plugincenter.dto.*
import net.mamoe.mirai.plugincenter.event.PluginModifiedEvent
import net.mamoe.mirai.plugincenter.model.PermissionEntity
import net.mamoe.mirai.plugincenter.repo.PluginRepo
import net.mamoe.mirai.plugincenter.services.LogService
import net.mamoe.mirai.plugincenter.services.PluginDescService
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import net.mamoe.mirai.plugincenter.utils.state
import net.mamoe.mirai.plugincenter.utils.validate.requires
import net.mamoe.mirai.plugincenter.utils.withExchange
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import springfox.documentation.annotations.ApiIgnore

const val ADMIN_BASE_URL = "/v1/admin"

@RestController       // FIXME: /v1/admin
@RequestMapping(ADMIN_BASE_URL)
@Api(tags = ["管理员服务"], position = 3)
class AdminController(
    private val pluginRepo: PluginRepo,
    private val desc: PluginDescService,
    private val logger: LogService,
) {
    @Order(1)
    @ApiOperation("修改插件状态")
    @PatchMapping("/setstate")       // FIXME: setState ?
    fun setPluginState(@RequestBody setStateDto: SetStateDto, @ApiIgnore ctx: ServerWebExchange): ApiResp<*> {
        withExchange(ctx) {
            requires {
                loginUser can PermissionEntity.WritePlugin
            }
        }

        val user = ctx.loginUserOrReject

        val plugin = pluginRepo.findByPluginId(setStateDto.pluginId) ?: throw ExceptionResponse(HttpStatus.NOT_FOUND, "插件不存在")

        logger.newLog(null, user, PluginModifiedEvent(plugin.state, setStateDto.state), PluginModifiedEvent::class)

        desc.update(plugin) {
            this.state = setStateDto.state
        }

        return r.ok("成功")
    }

    @Order(2)
    @ApiOperation("插件列表")
    @GetMapping("/plugins")
    fun listAll(@RequestParam(required = false, defaultValue = "0") page: Int, @ApiIgnore ctx: ServerWebExchange): ApiResp<List<PluginDesc>> {
        withExchange(ctx) {
            requires {
                loginUser can PermissionEntity.ReadPluginList
            }
        }

        val user = ctx.loginUserOrReject

        return ApiResp.ok(desc.getList(page).map { it.toDto() })
    }


    @ApiOperation("获取插件数")
    @GetMapping("/plugins/count")
    fun count() = r.ok(PluginCount(desc.count()))
}
