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
import net.mamoe.mirai.plugincenter.model.PluginEntity

/**
 * PluginDescription
 */
@ApiModel
data class PluginDesc(
    @ApiModelProperty("编号") val id: Int,
    @ApiModelProperty("插件 ID") val pluginId: String,
    @ApiModelProperty("名称") val name: String,
    @ApiModelProperty("描述") val info: String,
    @ApiModelProperty("上传者") val owner: UserDto,
)

fun PluginEntity.toDto(): PluginDesc {
    return PluginDesc(id, pluginId, name, info, userByOwner.toDto())
}