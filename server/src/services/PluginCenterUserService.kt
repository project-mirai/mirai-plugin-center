/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.services

import kotlinx.coroutines.reactor.mono
import net.mamoe.mirai.plugincenter.repo.UserRepo
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class PluginCenterUserService(private val userRepo: UserRepo) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return mono {
            userRepo.findUserEntityByEmail(username).run { User(username, password, listOf()) }
        }
    }

    // override fun updatePassword(user: UserDetails?, newPassword: String?): Mono<UserDetails> {
    //     TODO("Not yet implemented")
    // }
}
