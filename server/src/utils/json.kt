/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.utils

import com.fasterxml.jackson.databind.json.JsonMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import net.mamoe.mirai.plugincenter.dto.ApiResp
import net.mamoe.mirai.plugincenter.serializer.ApiRespSerializer

val mpcJacksonModule = SimpleModule().also { module ->
    module.addSerializer(ApiResp::class.java, ApiRespSerializer(false))
}

val jsonMapper = JsonMapper().also { mapper ->
    mapper.registerModule(mpcJacksonModule)
}

fun Any.toJsonUseJackson(): String {
    return jsonMapper.writeValueAsString(this)
}
