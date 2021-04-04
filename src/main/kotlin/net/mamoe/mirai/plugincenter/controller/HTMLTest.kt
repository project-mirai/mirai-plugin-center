package net.mamoe.mirai.plugincenter.controller

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.reactor.asFlux
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable
import reactor.core.publisher.Flux
import springfox.documentation.annotations.ApiIgnore

@Controller
@ApiIgnore
class HTMLTest {

    @GetMapping("/index")
    suspend fun index(model: Model): String {
        val f = flow<String> {
           for (i in 1..3){
               delay(200)
               emit("Hello World")
           }
        }
        model.addAttribute("text", ReactiveDataDriverContextVariable(f, 1))
        return "index"
    }

}
