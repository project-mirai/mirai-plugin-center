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
import net.mamoe.mirai.plugincenter.dto.ApiResp
import net.mamoe.mirai.plugincenter.dto.LoginDTO
import net.mamoe.mirai.plugincenter.dto.RegisterDTO
import net.mamoe.mirai.plugincenter.dto.respOk
import net.mamoe.mirai.plugincenter.services.PluginCenterUserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import springfox.documentation.annotations.ApiIgnore
import javax.validation.Valid

@RestController
@Api(tags = ["认证服务"], position = 0)
@RequestMapping("/v1/sso")
class SSOController(private val userService: PluginCenterUserService) {
    @ApiOperation("登录")
    @PostMapping("/login")
    suspend fun login(@Valid @RequestBody login: LoginDTO): ApiResp<LoginDTO> {
        return respOk(login) // TODO 登录逻辑
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    suspend fun register(@Valid @RequestBody register: RegisterDTO, @ApiIgnore req: ServerWebExchange): ApiResp<Int> {
        return respOk(userService.registerUser(register, req.request.remoteAddress.toString()))
    }

}
