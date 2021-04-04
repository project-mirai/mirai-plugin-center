package net.mamoe.mirai.plugincenter.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import springfox.documentation.annotations.ApiIgnore

@RestController
@ApiIgnore
class HelloWorld {
    @GetMapping("/")
    suspend fun hello(): Any {
        return object {
            val hello = "123"
            val num = 1
        }
    }
}
