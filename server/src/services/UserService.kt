/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.services

import net.mamoe.mirai.plugincenter.dto.LoginDTO
import net.mamoe.mirai.plugincenter.dto.RegisterDTO
import net.mamoe.mirai.plugincenter.dto.ResetPasswordByEmailDTO
import net.mamoe.mirai.plugincenter.dto.ResetPasswordByPasswordDTO
import net.mamoe.mirai.plugincenter.entity.ResetPasswordTokenAndTime
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.repo.UserRepo
import net.mamoe.mirai.plugincenter.utils.href
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.naming.AuthenticationException
import kotlin.time.ExperimentalTime
import kotlin.time.minutes


@Service
class UserService(

    private val userRepo: UserRepo,

    private val bcrypt: BCryptPasswordEncoder,

    @Autowired
    private val mailService: MailService,

    @Value("\${mail.base.url}")
    val url: String
) {


    fun loadUserByUsername(username: String): UserEntity? {

        // todo 用户角色判断
        return userRepo.findUserEntityByEmail(username)

    }

    fun updatePassword(user: UserEntity, newPassword: String): UserEntity {
        // TODO: Should we check newPassword?
        if (bcrypt.matches(newPassword, user.password)) throw AuthenticationException("新密码不能与旧密码相同")
        val newEncodedPwd = bcrypt.encode(newPassword)
        // TODO: Should we save ip and date of password updating?
        return userRepo.save(user.apply { this.password = newEncodedPwd })

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

        mailService.sendMsg {
            subject = "Password Reset"
            to = user.email
            html = mailService.mailTemplate {
                greetingWithName("${user.nick} 你好")
                header("找回密码")
                content(
                    "您正在重置您在我们站点的密码,请点击${href("这里", "$url/browser/resetPassword?token=$uuid")}",
                    "如果非本人操作,请忽略本邮件",
                )
            }
        }
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
        updatePassword(user, reset.password)
        tokens.remove(user)
        return tokens
    }

    fun resetPassword(reset: ResetPasswordByPasswordDTO, user: UserEntity) {
        if (user.email != reset.email) throw AuthenticationException("你只能改你自己的密码")
        if (reset.password == reset.newPassword) throw AuthenticationException("你在改啥???")
        if (!bcrypt.matches(reset.password, user.password)) throw AuthenticationException("密码不正确")
        updatePassword(user, reset.newPassword)
    }


}
