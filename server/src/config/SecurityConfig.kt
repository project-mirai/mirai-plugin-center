/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.config

import org.springframework.context.annotation.Bean
import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.authorization.ReactiveAuthorizationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authorization.AuthorizationContext
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
class SecurityConfig {
    object MPCAuthMgr : ReactiveAuthorizationManager<AuthorizationContext> {
        fun needAuthed(exchange: ServerWebExchange): Boolean {
            // TODO:
            return false
        }

        override fun check(authentication: Mono<Authentication>, context: AuthorizationContext): Mono<AuthorizationDecision> {
            /*context.exchange.session.map { session->
            }*/
            // TODO: Session Auth
            context.exchange.request.headers["Authorization"]?.firstOrNull()?.let { authorization ->
                if (authorization == "token TEST_TOKEN") {
                    context.exchange.attributes["User"] = "Hso188moe" // TODO: Change to User Model
                    return authentication.map {
                        it.isAuthenticated = true
                        AuthorizationDecision(true)
                    }
                }
            }
            return Mono.just(AuthorizationDecision(!needAuthed(context.exchange)))
        }

    }

    @Bean
    fun bcryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun springSecurityFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        http.authorizeExchange().pathMatchers("/**").access(MPCAuthMgr)
        http.authorizeExchange().pathMatchers("/**").permitAll()  //暂时授权所有请求
        http.formLogin().disable()
        http.csrf().disable()
        return http.build()
    }
}
