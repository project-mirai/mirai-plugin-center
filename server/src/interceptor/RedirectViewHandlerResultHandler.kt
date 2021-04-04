/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.interceptor

import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.reactive.HandlerResult
import org.springframework.web.reactive.HandlerResultHandler
import org.springframework.web.reactive.result.view.RedirectView
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
@Order(0)
class RedirectViewHandlerResultHandler : HandlerResultHandler {
    override fun supports(result: HandlerResult): Boolean {
        return result.returnValue is RedirectView
    }

    override fun handleResult(exchange: ServerWebExchange, result: HandlerResult): Mono<Void> {
        val v = result.returnValue as RedirectView
        return Mono.create { callback ->
            exchange.response.statusCode = v.statusCode
            exchange.response.headers.add("Location", v.url)
            callback.success()
        }
    }
}