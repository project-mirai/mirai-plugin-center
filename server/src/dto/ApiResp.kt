/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.dto

import net.mamoe.mirai.plugincenter.utils.toJsonUseJackson

open class ApiResp(
    val code: Int,
    val message: String? = null,
    val response: Any? = null
) {
    interface Serialized {
        val rawString: String
    }

    public class SerializedApiResp(code: Int, message: String? = null, response: Any? = null) : ApiResp(code, message, response),
        Serialized {
        override val rawString: String by lazy {
            this.toJsonUseJackson()
        }
    }

    companion object {
        val NOT_AUTHED = SerializedApiResp(403, "Not authed")
        val NOT_FOUND = SerializedApiResp(404, "Resource not found")
    }
}



