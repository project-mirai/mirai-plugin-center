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
import net.mamoe.mirai.plugincenter.model.UserEntity
import javax.validation.constraints.Email

@ApiModel
data class LoginDTO(
    @ApiModelProperty("邮箱") @Email val email: String,
    @ApiModelProperty("密码") val password: String
)

@ApiModel
data class RegisterDTO(
    @ApiModelProperty("邮箱") @Email val email: String,
    @ApiModelProperty("昵称") val nick: String,
    @ApiModelProperty("密码") val password: String
)

@ApiModel
data class LoginSuccessDTO(@ApiModelProperty("登录令牌") val token: String)

@ApiModel
data class UserDto(
    @ApiModelProperty("邮箱") @Email val email: String,
    @ApiModelProperty("昵称") val nick: String,
)

fun UserEntity.toDto(): UserDto = UserDto(email, nick)