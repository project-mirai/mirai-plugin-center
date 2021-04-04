/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.interceptor

import net.mamoe.mirai.plugincenter.dto.ApiResp
import org.reactivestreams.Publisher
import org.springframework.core.ResolvableType
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ReactiveHttpOutputMessage
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import reactor.core.publisher.Mono

internal class MPCWrappedHttpMessageWriter<T>(
    private val delegate: HttpMessageWriter<T>
) : HttpMessageWriter<T> by delegate {
    override fun write(
        inputStream: Publisher<out T>,
        elementType: ResolvableType,
        mediaType: MediaType?,
        message: ReactiveHttpOutputMessage,
        hints: MutableMap<String, Any>
    ): Mono<Void> {
        return delegate.write(overridePublisher(inputStream, message as ServerHttpResponse), elementType, mediaType, message, hints)
    }

    private fun overridePublisher(
        o: Publisher<out T>,
        response: ServerHttpResponse
    ): Publisher<out T> {
        if (o is Mono<*>) {
            @Suppress("UNCHECKED_CAST")
            return o.map { value ->
                if (value is ApiResp<*>) {
                    response.statusCode = HttpStatus.resolve(value.code)
                }
                value
            } as Publisher<out T>
        }
        return o
    }

    override fun write(
        inputStream: Publisher<out T>,
        actualType: ResolvableType,
        elementType: ResolvableType,
        mediaType: MediaType?,
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        hints: MutableMap<String, Any>
    ): Mono<Void> {
        return delegate.write(overridePublisher(inputStream, response), actualType, elementType, mediaType, request, response, hints)
    }
}
