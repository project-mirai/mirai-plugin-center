/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.controller

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import springfox.documentation.annotations.ApiIgnore

@Controller
@ApiIgnore
class HTMLTest {

    @GetMapping("/index")
    suspend fun index(model: Model): String {
        val f = flow<String> {
            for (i in 1..3) {
                delay(200)
                emit("Hello World")
            }
        }
        model.addAttribute("text", ReactiveDataDriverContextVariable(f, 1))
        return "index"
    }

}
