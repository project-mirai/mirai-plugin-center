/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.utils

import net.mamoe.mirai.plugincenter.model.PermissionEntity
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.services.UserService
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.server.ServerWebExchange
import java.lang.IllegalStateException

const val SessionUserKey = "User"

data class SessionLoginUser(val uid: Int, val userSvc: UserService)

val ServerWebExchange.loginUser: UserEntity?
    get() = (attributes[SessionUserKey] as? SessionLoginUser)?.let {
        it.userSvc.findUserById(it.uid)
    }

val ServerWebExchange.loginUserOrReject: UserEntity
    get() = loginUser ?: throw AccessDeniedException(authFailedReason.msg)

val ServerWebExchange.authFailedReason: AuthFailedReason
    get() = attributes["AuthFailedReason"] as? AuthFailedReason ?: AuthFailedReason.UNKNOWN

fun ServerWebExchange.setSessionAccount(user: SessionLoginUser) {
    session.subscribe { session ->
        session.attributes[SessionUserKey] = user
    }
}

fun ServerWebExchange.removeSessionAccount() {
    session.subscribe { session ->
        session.attributes.remove(SessionUserKey)
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

/**
 * 针对 [ServerWebExchange] 的检查块实现
 *
 * **线程不安全**
 */
class RequireBlock(private val exchange: ServerWebExchange) {
    private var loginUser: UserEntity? = null

    /**
     * 要求用户登录
     *
     * @throws AccessDeniedException 由 [ServerWebExchange.loginUserOrReject] 抛出
     */
    fun login(): UserEntity {
        loginUser = exchange.loginUserOrReject

        return loginUser!!      // safe unless Kotlin is stupid
                                //             ^Warning: condition always true
    }

    /**
     * 需要在 login 后调用，否则抛出异常
     *
     * @throws IllegalStateException 未调用 login 时调用该方法抛出
     */
    fun permission(vararg permission: PermissionEntity): List<PermissionEntity> {
        val user = loginUser
            TODO()
        if (user != null) {
            //
        } else throw IllegalStateException("login() required")
    }
}

fun ServerWebExchange.requires(block: RequireBlock.() -> Unit) {

}