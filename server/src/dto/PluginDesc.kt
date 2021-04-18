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
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.mirai.plugincenter.dto.PluginDesc.Companion.INFO_EXAMPLE
import net.mamoe.mirai.plugincenter.dto.PluginDesc.Companion.NAME_EXAMPLE
import net.mamoe.mirai.plugincenter.model.PluginEntity
import org.springframework.core.annotation.Order

/**
 * PluginDescription
 */
@Serializable
@ApiModel("插件信息")
data class PluginDesc(
    @Order(1)
    @SerialName("id")
    @ApiModelProperty("插件 ID", name = "id", example = ID_EXAMPLE)
    @get:JvmName("getId")
    val pluginId: String,

    @Order(2)
    @ApiModelProperty("名称", example = NAME_EXAMPLE)
    val name: String,

    @Order(3)
    @ApiModelProperty("描述", allowEmptyValue = true)
    val info: String = "",

    @Order(4)
    @ApiModelProperty("上传者", accessMode = ApiModelProperty.AccessMode.READ_ONLY)
    val owner: UserDto? = null,

    @Order(5)
    @ApiModelProperty("状态", accessMode = ApiModelProperty.AccessMode.READ_ONLY, allowableValues = "Accepted, Denied")
    val status: PluginEntity.Status = PluginEntity.Status.Denied
) {
    companion object {
        const val ID_EXAMPLE = "org.example.mirai.test-plugin"
        const val NAME_EXAMPLE = "A Simple Test Plugin"
        const val INFO_EXAMPLE = "This is a sample plugin providing some features."
    }
}

/**
 * 在更新时使用
 */
@Serializable
@ApiModel("插件信息")
data class PluginDescUpdate(
    @Order(1)
    @ApiModelProperty("名称", example = NAME_EXAMPLE)
    val name: String? = null,

    @Order(2)
    @ApiModelProperty("描述", example = INFO_EXAMPLE)
    val info: String? = null,
)

@Serializable
@ApiModel("插件状态信息")
data class PluginStatusUpdate(
    @Order(1)
    @ApiModelProperty("状态", allowableValues = "Accepted, Denied")
    val status: PluginEntity.Status,
)

fun PluginEntity.toDto(): PluginDesc {
    return PluginDesc(pluginId, name, info, userByOwner.toDto(),
        PluginEntity.Status.values().getOrElse(this.status) { PluginEntity.Status.Denied })
}

fun PluginEntity.copyFrom(desc: PluginDesc): PluginEntity {
    pluginId = desc.pluginId
    name = desc.name
    info = desc.info
    return this
}