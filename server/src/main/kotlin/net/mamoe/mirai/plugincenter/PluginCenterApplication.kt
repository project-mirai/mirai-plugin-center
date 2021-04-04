package net.mamoe.mirai.plugincenter

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PluginCenterApplication

fun main(args: Array<String>) {
	runApplication<PluginCenterApplication>(*args)
}
