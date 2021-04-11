/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.dto

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty
import kotlinx.serialization.Serializable
import net.mamoe.mirai.plugincenter.model.UserEntity
import org.springframework.core.annotation.Order
import javax.validation.constraints.Email

@ApiModel("登录信息")
data class LoginDTO(
    @ApiModelProperty("邮箱", example = "foo@example.com") @field:Email val email: String,
    @ApiModelProperty("密码") val password: String
)

@ApiModel("注册信息")
data class RegisterDTO(
    @ApiModelProperty("邮箱", example = "foo@example.com") @field:Email val email: String,
    @ApiModelProperty("昵称") val nick: String,
    @ApiModelProperty("密码") val password: String
)

@ApiModel("用邮箱密码重置信息")
data class ResetPasswordByEmailDTO(
    @ApiModelProperty("Token") val token: String,
    @ApiModelProperty("邮箱", example = "foo@example.com") @field:Email val email: String,
    @ApiModelProperty("密码") val password: String
)

@ApiModel("修改密码信息")
data class ResetPasswordByPasswordDTO(
    @ApiModelProperty("邮箱", example = "foo@example.com") @field:Email val email: String,
    @ApiModelProperty("密码(旧)") val password: String,
    @ApiModelProperty("密码(新)") val newPassword: String
)

@Serializable
@ApiModel("用户信息")
data class UserDto(
    @Order(0)
    @ApiModelProperty("邮箱", example = "foo@example.com")
    @field:Email
    val email: String,

    @Order(1)
    @ApiModelProperty("昵称", example = "Tester")
    val nick: String,
)

fun UserEntity.toDto(): UserDto = UserDto(email, nick)
