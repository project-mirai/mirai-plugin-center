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
import io.swagger.annotations.ApiParam
import net.mamoe.mirai.plugincenter.dto.*
import net.mamoe.mirai.plugincenter.model.PluginEntity
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.services.ManagerService
import net.mamoe.mirai.plugincenter.utils.isManager
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/v1/manager")
@Api(tags = ["管理员服务"], position = 3)
class ManagerController(
    val manager: ManagerService
) {


    private fun withManager(user: UserEntity, block: UserEntity.() -> Resp): Resp {
        return if (user.isManager()) {
            user.block()
        } else {
            Resp.FORBIDDEN
        }
    }

    @ApiOperation("修改插件状态")
    @PostMapping("/modify/{id}")
    fun modifyStatus(
        @ApiParam("插件 ID", example = PluginDesc.ID_EXAMPLE)
        @PathVariable
        id: String,

        @RequestBody
        update: PluginStatusUpdate,

        @ApiIgnore
        exchange: ServerWebExchange
    ): Resp {
        val user = exchange.loginUserOrReject
        val status = PluginEntity.Status
            .values()
            .getOrNull(update.status) ?: return Resp.BAD_REQUEST

        return withManager(user) {
            val ret = manager.modifyStatus(id, status)

            if (ret == null) {      // TODO: enrich error message
                Resp.NOT_FOUND
            } else {
                Resp.OK
            }
        }
    }
}