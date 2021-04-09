/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.config

import net.mamoe.mirai.plugincenter.interceptor.MPCWrappedHttpMessageWriter
import org.springframework.beans.factory.config.BeanPostProcessor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.codec.EncoderHttpMessageWriter
import org.springframework.http.codec.HttpMessageWriter
import org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler

@Configuration
class BeanEditConfig {
    private fun replaceWriter(w: HttpMessageWriter<*>): HttpMessageWriter<*>? {
        return MPCWrappedHttpMessageWriter(w)
    }

    @Bean
    fun entityManagerBeanPostProcessor(): BeanPostProcessor? {
        return object : BeanPostProcessor {
            @Throws(BeansException::class)
            override fun postProcessBeforeInitialization(bean: Any, beanName: String): Any {
                return bean
            }

            @Throws(BeansException::class)
            override fun postProcessAfterInitialization(bean: Any, beanName: String): Any {
                if (bean is ResponseBodyResultHandler) {
                    val writers = bean.messageWriters
                    writers.replaceAll { writer: HttpMessageWriter<*>? ->
                        if (writer is EncoderHttpMessageWriter<*>) {
                            return@replaceAll replaceWriter(writer)
                        }
                        writer
                    }
                }
                return bean
            }
        }
    }

}