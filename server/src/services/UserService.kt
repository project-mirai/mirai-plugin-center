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
import kotlinx.coroutines.runInterruptible
import net.mamoe.mirai.plugincenter.dto.LoginDTO
import net.mamoe.mirai.plugincenter.dto.RegisterDTO
import net.mamoe.mirai.plugincenter.dto.UserDto
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.repo.UserRepo
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import javax.naming.AuthenticationException


@Service
class UserService(private val userRepo: UserRepo, private val bcrypt: BCryptPasswordEncoder) {
    fun loadUserByUsername(username: String):UserEntity?{

        // todo 用户角色判断
        return userRepo.findUserEntityByEmail(username)

    }

    suspend fun updatePassword(user: UserEntity, newPassword: String): UserEntity {
        // TODO: Should we check newPassword?

        return runInterruptible {
            val newEncodedPwd = bcrypt.encode(newPassword)

            // TODO: Should we save ip and date of password updating?
            userRepo.save(user.apply {
                this.password = newEncodedPwd
            })
        }
    }

    @Throws(AuthenticationException::class)
    suspend fun registerUser(user: RegisterDTO, ip: String): UserEntity {
        if (userRepo.existsByEmail(user.email)) {
            throw AuthenticationException("邮箱已被使用")
        }
        val encodedPwd = bcrypt.encode(user.password)
        return runInterruptible {
//            userRepo.registerUser(user.nick, user.email, encodedPwd, "fuck", 1, Timestamp(System.currentTimeMillis()))
            userRepo.save(UserEntity().apply {
                email = user.email
                password = encodedPwd
                nick = user.nick
                registerIp = ip
                role = 1
            })

        }
    }

    suspend fun login(user: LoginDTO): UserEntity? {
        val sqlUser = loadUserByUsername(user.email) ?: return null
        if (bcrypt.matches(user.password, sqlUser.password)) {

            return sqlUser
        }
        return null
    }


}
