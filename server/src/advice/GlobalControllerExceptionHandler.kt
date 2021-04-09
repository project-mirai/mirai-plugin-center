/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.advice

import net.mamoe.mirai.plugincenter.dto.ApiResp
import net.mamoe.mirai.plugincenter.dto.Resp
import net.mamoe.mirai.plugincenter.utils.authFailedReason
import org.slf4j.Logger
import org.springframework.beans.BeansException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.NestedRuntimeException
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import javax.naming.AuthenticationException

class ExceptionResponse(
    val code: Int,
    override val message: String,
    override val cause: Throwable? = null,
) : Exception() {
    constructor(code: HttpStatus, message: String, cause: Throwable? = null) : this(code.value(), message, cause)
}

@ControllerAdvice
class GlobalControllerExceptionHandler {

    @Autowired
    private lateinit var logger: Logger


    @ResponseBody
    @ExceptionHandler(AccessDeniedException::class)
    fun handle(exchange: ServerWebExchange, e: AccessDeniedException): ApiResp<Unit> {
        if (logger.isTraceEnabled) {
            logger.trace(e.toString(), e)
        }
        return ApiResp(
            code = Resp.FORBIDDEN.code,
            message = exchange.authFailedReason.msg,
            null
        ) { e.stackTraceToString() }
    }

    @ResponseBody
    @ExceptionHandler(Exception::class)
    fun handle(e: Exception): ApiResp<Unit> {
        if (logger.isErrorEnabled) {
            logger.error(e.toString(), e)
        }

        when (e) {
            is ExceptionResponse -> return ApiResp(e.code, e.message, null) { e.stackTraceToString() }
            is NestedRuntimeException -> return ApiResp(400, e.mostSpecificCause.toString(), null) { e.stackTraceToString() }
            is BeansException -> return ApiResp(400, e.message ?: e.toString(), null) { e.stackTraceToString() }
        }

        return ApiResp(500, e.message ?: e.toString(), null) { e.stackTraceToString() }
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handle(e: IllegalArgumentException): ApiResp<Unit> {
        if (logger.isDebugEnabled) {
            logger.debug(e.toString(), e)
        }
        return ApiResp(400, e.message ?: e.toString(), null)
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(WebExchangeBindException::class)
    fun handleBadRequest(e: WebExchangeBindException): ApiResp<Unit> {
        if (logger.isDebugEnabled) {
            logger.debug(e.toString(), e)
        }
        return ApiResp(400, e.bindingResult.allErrors.joinToString { "${it.objectName}: ${it.defaultMessage}" }, null)
    }


    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AuthenticationException::class)
    fun handleBadRequest(e: AuthenticationException): ApiResp<Unit> {
        if (logger.isDebugEnabled) {
            logger.debug(e.toString(), e)
        }
        return ApiResp(403,e.message )
    }

}
