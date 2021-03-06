/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.utils

import net.mamoe.mirai.plugincenter.model.UserEntity
import org.springframework.web.server.ServerWebExchange

class ServerWebExchangeScope(val exchange: ServerWebExchange) {
    val loginUser: UserEntity by lazy { exchange.loginUserOrReject }
}

inline fun withExchange(exchange: ServerWebExchange, block: ServerWebExchangeScope.() -> Unit) {
    ServerWebExchangeScope(exchange).block()
}

inline fun ServerWebExchange.runScope(block: ServerWebExchangeScope.() -> Unit) = withExchange(this, block)