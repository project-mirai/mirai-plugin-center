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
import net.mamoe.mirai.plugincenter.dto.LoginSuccessDTO
import net.mamoe.mirai.plugincenter.dto.RegisterDTO
import net.mamoe.mirai.plugincenter.services.DecideService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(tags = ["认证服务"])
@RequestMapping("/v1/sso")
class SSOController(
    @Autowired
    val decideService: DecideService
) {

    @ApiOperation("登录")
    @PostMapping("/login")
    fun login(@RequestBody login: LoginDTO): ApiResp<LoginSuccessDTO> {
        TODO()
    }

    @ApiOperation("注册")
    @PostMapping("/register")
    fun register(@RequestBody register: RegisterDTO): ApiResp<String> {
        if (!decideService.isEmail(register.email)) return ApiResp(403, "不是邮箱类型", null)

        if (register.password.length < 8 || register.password.length > 20) return ApiResp(403, "密码小于8位或大于20", null)

        return ApiResp(403, register.email, null)
    }

}
