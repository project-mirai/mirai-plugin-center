/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.controller

import net.mamoe.mirai.plugincenter.dto.resp
import net.mamoe.mirai.plugincenter.dto.toBrowserResponse
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import springfox.documentation.annotations.ApiIgnore

@RestController
@RequestMapping("/browser")
@ApiIgnore
class FrontEndBridgeController {

    @GetMapping("/whoami")
    fun whoami(exchange: ServerWebExchange): Any {
        val usr = exchange.loginUserOrReject
        return resp {
            "whoami" - mapOf(
                "nick" to usr.nick,
                "role" to usr.role,
            )
        }.toApiResp<Any>().toBrowserResponse(exchange)
    }
}
