/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import net.mamoe.mirai.plugincenter.dto.*
import net.mamoe.mirai.plugincenter.entity.ResetPasswordTokenAndTime
import net.mamoe.mirai.plugincenter.model.UserEntity
import net.mamoe.mirai.plugincenter.services.MailService
import net.mamoe.mirai.plugincenter.services.UserService
import net.mamoe.mirai.plugincenter.utils.setSessionAccount
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import springfox.documentation.annotations.ApiIgnore
import java.util.concurrent.ConcurrentHashMap
import javax.naming.AuthenticationException
import javax.validation.Valid
import javax.validation.constraints.Email
import kotlin.time.ExperimentalTime

@RestController
@Validated
@Api(tags = ["认证服务"], position = 0)
@RequestMapping("/v1/sso")
class SSOController(private val userService: UserService, private val mailService: MailService) {

    private var tokenMap = ConcurrentHashMap<UserEntity, ResetPasswordTokenAndTime>()


    @ApiOperation("登录")
    @PostMapping("/login")
    fun login(@Valid @RequestBody login: LoginDTO, @ApiIgnore req: ServerWebExchange): ApiResp<UserDto> {
        val result = userService.login(login)
        if (result != null) {
            req.setSessionAccount(result)
            return respOk(result.toDto())
        } else {
            throw AuthenticationException("登录失败")
        }
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    fun register(@Valid @RequestBody register: RegisterDTO, @ApiIgnore req: ServerWebExchange): ApiResp<UserDto> {
        val result = userService.registerUser(register, req.request.remoteAddress.toString())
        req.setSessionAccount(result)
        return respOk(result.toDto())
    }

    @OptIn(ExperimentalTime::class)
    @ApiOperation("修改密码")
    @PatchMapping("/resetPassword")
    fun resetPassword(@RequestBody resetPassword: ResetPasswordByPasswordDTO): ApiResp<String> {
        userService.resetPassword(resetPassword)
        return respOk("ok")
    }

    @OptIn(ExperimentalTime::class)
    @ApiOperation("发送找回密码邮件")
    @GetMapping("/resetPassword")
    fun resetPassword(@RequestParam(name = "email") @Email email: String): ApiResp<String> {
        tokenMap = userService.resetPassword(email, tokenMap)
        return respOk("ok")
    }

    @OptIn(ExperimentalTime::class)
    @ApiOperation("找回密码")
    @PostMapping("/resetPassword")
    fun resetPassword(@RequestBody resetPassword: ResetPasswordByEmailDTO): ApiResp<String> {
        tokenMap = userService.resetPassword(resetPassword, tokenMap)
        return respOk("ok")
    }


}
