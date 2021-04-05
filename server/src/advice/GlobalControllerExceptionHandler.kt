/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.advice

import net.mamoe.mirai.plugincenter.PluginCenterApplication
import net.mamoe.mirai.plugincenter.dto.ApiResp
import org.springframework.beans.BeansException
import org.springframework.core.NestedRuntimeException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalControllerExceptionHandler {
    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ApiResp<Unit> {
        PluginCenterApplication.logger.error(e.toString(), e)


        when (e) {
            is NestedRuntimeException -> return ApiResp(400, e.mostSpecificCause.toString(), null, e.stackTraceToString())
            is BeansException -> return ApiResp(400, e.message ?: e.toString(), null, e.stackTraceToString())
        }

        return ApiResp(500, e.message ?: e.toString(), null, e.stackTraceToString())
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handle(e: IllegalArgumentException): ApiResp<Unit> {
        PluginCenterApplication.logger.trace(e.toString(), e)
        return ApiResp(400, e.message ?: e.toString(), null)
    }
}

class NeedEmailException(message: String?) : Exception(message) {
    constructor() : this("请输入正确的邮箱")
    override val message: String?
        get() = super.message
}


