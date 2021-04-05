/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.services

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.withContext
import net.mamoe.mirai.plugincenter.dto.RegisterDTO
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.repo.UserRepo
import org.springframework.security.core.userdetails.ReactiveUserDetailsPasswordService
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.validation.annotation.Validated
import reactor.core.publisher.Mono
import java.sql.Timestamp

@Service
class PluginCenterUserService(private val userRepo: UserRepo,private val bcrypt: BCryptPasswordEncoder) : ReactiveUserDetailsService, ReactiveUserDetailsPasswordService {
    override fun findByUsername(username: String): Mono<UserDetails> {
        return mono {
            userRepo.findUserEntityByEmail(username).run { User(username, password, listOf()) }
        }
    }

    override fun updatePassword(user: UserDetails?, newPassword: String?): Mono<UserDetails> {
        TODO("Not yet implemented")
    }

    suspend fun registerUser( user: RegisterDTO): Int {
        val encodedPwd = bcrypt.encode(user.password)
        return withContext(Dispatchers.IO) {
            userRepo.registerUser(user.username,user.email,encodedPwd,"fuck",1, Timestamp(System.currentTimeMillis()))
        }

    }
}
