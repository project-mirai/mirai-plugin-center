package net.mamoe.mirai.plugincenter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.oas.annotations.EnableOpenApi

@SpringBootApplication
class PluginCenterApplication

fun main(args: Array<String>) {
	runApplication<PluginCenterApplication>(*args)
}
