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
import net.mamoe.mirai.plugincenter.dto.ResetPasswordByEmailDTO
import net.mamoe.mirai.plugincenter.dto.ResetPasswordByPasswordDTO
import net.mamoe.mirai.plugincenter.entity.ResetPasswordTokenAndTime
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.repo.UserRepo
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.naming.AuthenticationException
import kotlin.time.ExperimentalTime
import kotlin.time.minutes


@Service
class UserService(private val userRepo: UserRepo, private val bcrypt: BCryptPasswordEncoder) {
    fun loadUserByUsername(username: String): UserEntity? {

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
    fun registerUser(user: RegisterDTO, ip: String): UserEntity {
        if (userRepo.existsByEmail(user.email)) {
            throw AuthenticationException("邮箱已被使用")
        }
        val encodedPwd = bcrypt.encode(user.password)
//            userRepo.registerUser(user.nick, user.email, encodedPwd, "fuck", 1, Timestamp(System.currentTimeMillis()))
        return userRepo.save(UserEntity().apply {
            email = user.email
            password = encodedPwd
            nick = user.nick
            registerIp = ip
            role = 1
        })


    }

    fun login(user: LoginDTO): UserEntity? {
        val sqlUser = loadUserByUsername(user.email) ?: return null
        if (bcrypt.matches(user.password, sqlUser.password)) {

            return sqlUser
        }
        return null
    }

    @ExperimentalTime
    private fun clearResetToken(tokensMap: ConcurrentHashMap<UserEntity, ResetPasswordTokenAndTime>): ConcurrentHashMap<UserEntity, ResetPasswordTokenAndTime> {
        for (key: UserEntity in tokensMap.keys()) {
            if (tokensMap[key]!!.time < System.currentTimeMillis() + 30.minutes.toLongMilliseconds()) tokensMap.remove(key)
        }
        return tokensMap
    }

    @OptIn(ExperimentalTime::class)
    fun resetPassword(
        email: String,
        tokenMap: ConcurrentHashMap<UserEntity, ResetPasswordTokenAndTime>
    ): ConcurrentHashMap<UserEntity, ResetPasswordTokenAndTime> {
        val tokens = clearResetToken(tokenMap)
        val uuid = UUID.randomUUID()
        val user = loadUserByUsername(email) ?: throw AuthenticationException("用户不存在")
        if (tokens[user] != null) tokens.remove(user)
        tokens[user] = ResetPasswordTokenAndTime(uuid)

        //TODO 发送找回密码邮件 localhost/v1/sso/resetPassword [POST]

        println(uuid)

        return tokens
    }

    @OptIn(ExperimentalTime::class)
    fun resetPassword(
        reset: ResetPasswordByEmailDTO,
        tokenMap: ConcurrentHashMap<UserEntity, ResetPasswordTokenAndTime>
    ): ConcurrentHashMap<UserEntity, ResetPasswordTokenAndTime> {
        val tokens = clearResetToken(tokenMap)
        val user = loadUserByUsername(reset.email) ?: throw AuthenticationException("用户不存在")
        if (tokens[user] == null) throw AuthenticationException("重置失败")
        if (bcrypt.matches(reset.password, user.password)) throw AuthenticationException("新密码不能与旧密码相同")
        mono { updatePassword(user, reset.password) }
        tokens.remove(user)
        return tokens
    }

    fun resetPassword(reset: ResetPasswordByPasswordDTO) {
        if (reset.password == reset.newPassword) throw AuthenticationException("你在改啥???")
        val user = loadUserByUsername(reset.email) ?: throw AuthenticationException("用户不存在")
        if (bcrypt.matches(reset.newPassword, user.password)) throw AuthenticationException("新密码不能与旧密码相同")
        if (!bcrypt.matches(reset.password, user.password)) throw AuthenticationException("密码不正确")
        else mono { updatePassword(user, reset.newPassword) }
    }


}
