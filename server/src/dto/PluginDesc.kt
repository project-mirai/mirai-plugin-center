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
import io.swagger.annotations.ApiModelProperty.AccessMode.READ_ONLY
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.plugincenter.model.PluginEntity

/**
 * PluginDescription
 */
@Serializable
@ApiModel
data class PluginDesc(
    @SerialName("id") @ApiModelProperty("插件 ID", name = "id", example = "org.example.mirai.test-plugin") val pluginId: String,
    @ApiModelProperty("名称", example = "A Simple Test Plugin") val name: String,
    @ApiModelProperty("描述", allowEmptyValue = true) val info: String = "",
    @ApiModelProperty("上传者", accessMode = READ_ONLY) val owner: UserDto? = null,
)

fun PluginEntity.toDto(): PluginDesc {
    return PluginDesc(pluginId, name, info, userByOwner.toDto())
}

fun PluginEntity.copyFrom(desc: PluginDesc): PluginEntity {
    pluginId = desc.pluginId
    name = desc.name
    info = desc.info
    return this
}