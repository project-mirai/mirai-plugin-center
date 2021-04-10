/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.services

import net.mamoe.mirai.plugincenter.dto.ApiResp
import net.mamoe.mirai.plugincenter.dto.Resp
import net.mamoe.mirai.plugincenter.entity.ResetPasswordTokenAndTime
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.repo.TokenRepo
import net.mamoe.mirai.plugincenter.repo.UserRepo
import net.mamoe.mirai.plugincenter.utils.AuthFailedReason
import net.mamoe.mirai.plugincenter.utils.authFailedReason
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.security.web.server.authorization.AuthorizationContext
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty

@Component
class AuthService(
    val tokenRepo: TokenRepo,
    val userRepo: UserRepo,
) : ReactiveAuthorizationManager<AuthorizationContext>,
    ServerAuthenticationEntryPoint {
    private fun fastComplete(context: AuthorizationContext): Boolean {
        // Website JavaScript/CSS/.... resources.

        if (context.exchange.request.uri.path.startsWith("/assets/")) {
            return true
        }
        when (context.exchange.request.uri.path) {
            "/favicon.ico",
            "/robots.txt",  // SEO
            "/sitemap.xml", // SEO
            -> {
                return true
            }
        }
        return false
    }

    fun needAuthed(exchange: ServerWebExchange): Boolean {
        // TODO:
        return false
    }

    override fun check(authentication: Mono<Authentication>, context: AuthorizationContext): Mono<AuthorizationDecision> {
        if (fastComplete(context)) {
            context.exchange.attributes["AuthFailedReason"] = AuthFailedReason.GUEST
            return Mono.just(AuthorizationDecision(true))
        }

        fun completeAuth(user: UserEntity): Mono<AuthorizationDecision> {
            context.exchange.attributes["User"] = user
            val trust = AuthorizationDecision(true)
            return authentication.map {
                it.isAuthenticated = true
                trust
            }.defaultIfEmpty(trust)
        }

        fun authFailed(reason: AuthFailedReason): Mono<AuthorizationDecision> {
            context.exchange.attributes["AuthFailedReason"] = reason
            return Mono.just(AuthorizationDecision(!needAuthed(context.exchange)))
        }
        @Suppress("RemoveExplicitTypeArguments")
        return context.exchange.session.flatMap<AuthorizationDecision> { session ->
            (session.attributes["User"] as? UserEntity)?.let { ue ->
                return@flatMap completeAuth(ue)
            }
            return@flatMap Mono.empty()
        }.switchIfEmpty {
            context.exchange.request.headers["Authorization"]?.firstOrNull()?.let { authorization ->
                val type = authorization.substringBefore(' ')
                val value = authorization.substringAfter(' ')
                when (type) {
                    "token" -> {
                        val token = tokenRepo.findTokenEntityByToken(value)
                            ?: return@switchIfEmpty authFailed(AuthFailedReason.TOKEN_NOT_FOUND)

                        val expireTime = token.expireTime.time
                        if (expireTime != 0L && expireTime < System.currentTimeMillis()) {
                            return@switchIfEmpty authFailed(AuthFailedReason.TOKEN_EXPIRED)
                        }

                        return@switchIfEmpty completeAuth(token.userByOwner)
                    }
                    else -> Unit
                }
            }
            return@switchIfEmpty Mono.empty()
        }.switchIfEmpty {
            authFailed(AuthFailedReason.GUEST)
        }
    }

    override fun commence(exchange: ServerWebExchange, ex: AuthenticationException?): Mono<Void> {
        val response = exchange.response
        return response.writeWith { writer ->
            response.statusCode = HttpStatus.FORBIDDEN
            response.headers.contentType = MediaType.APPLICATION_JSON

            val resp = ApiResp<Unit>(Resp.FORBIDDEN.code, exchange.authFailedReason.msg)
            val buffer = response.bufferFactory().wrap(resp.toJsonString().toByteArray())

            writer.onNext(buffer)
            writer.onComplete()
        }
    }



}
