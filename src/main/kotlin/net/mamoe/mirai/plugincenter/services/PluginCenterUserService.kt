package net.mamoe.mirai.plugincenter.services

import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PluginCenterUserService :ReactiveUserDetailsService,ReactiveUserDetailsPasswordService {
    override fun findByUsername(username: String?): Mono<UserDetails> {
        TODO("Not yet implemented")
    }

    override fun updatePassword(user: UserDetails?, newPassword: String?): Mono<UserDetails> {
        TODO("Not yet implemented")
    }
}
