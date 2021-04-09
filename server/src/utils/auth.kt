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
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.server.ServerWebExchange

val ServerWebExchange.loginUser: UserEntity?
    get() = attributes["User"] as? UserEntity

val ServerWebExchange.loginUserOrReject: UserEntity
    get() = loginUser ?: throw AccessDeniedException(authFailedReason.msg)

val ServerWebExchange.authFailedReason: AuthFailedReason
    get() = attributes["AuthFailedReason"] as? AuthFailedReason ?: AuthFailedReason.UNKNOWN

fun ServerWebExchange.setSessionAccount(user: UserEntity) {
    session.subscribe { session ->
        session.attributes["User"] = user
    }
}

enum class AuthFailedReason(val msg: String) {
    UNKNOWN("Unknown"),
    GUEST("Unauthorized"),
    BLOCKED("Blocked"),
    TOKEN_EXPIRED("Token expired"),
    TOKEN_NOT_FOUND("Invalid token"),
    UNKNOWN_USER("Invalid session"),
    ;
}
