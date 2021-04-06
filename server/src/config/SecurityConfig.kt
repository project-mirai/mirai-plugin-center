/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.config

import net.mamoe.mirai.plugincenter.services.AuthService
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import org.springframework.web.server.session.WebSessionManager
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun bcryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    // 虽然不知道为啥要 disable session 但是先 disable 了
    // TODO: Browser session auth
    @Bean(WebHttpHandlerBuilder.WEB_SESSION_MANAGER_BEAN_NAME)
    fun webSessionManager(): WebSessionManager {
        return WebSessionManager { Mono.empty() }
    }

    @Bean
    fun springSecurityFilterChain(
        http: ServerHttpSecurity,
        auth: AuthService,
    ): SecurityWebFilterChain {
        http.requestCache().requestCache(NoOpServerRequestCache.getInstance())  // 关闭session
        http.authorizeExchange().pathMatchers("/**").access(auth)
        http.exceptionHandling().authenticationEntryPoint(auth)
        http.httpBasic().disable()
        http.csrf().disable()
        return http.build()
    }
}
