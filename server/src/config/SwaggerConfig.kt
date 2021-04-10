/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.config

import net.mamoe.mirai.plugincenter.BuildConfig
import net.mamoe.mirai.plugincenter.PluginCenterApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.ApiInfoBuilder
import springfox.documentation.builders.ParameterBuilder
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestParameterBuilder
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.configuration.Swagger2WebFluxConfiguration
import java.text.SimpleDateFormat

@Configuration
class SwaggerConfig {

    private fun apiInfo() = ApiInfoBuilder()
        .title("Mirai 插件中心接口文档")
        .version(BuildConfig.VERSION)
        .description(
            """          
               编译日期${PluginCenterApplication.buildTime}
        """.trimIndent()
        )
        .contact(Contact("mamoe", "https://github.com/project-mirai/mirai-plugin-center", "null"))
        .build()

    @Bean
    fun swaggerDocket() = Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())

        .select()
        .build()




}
