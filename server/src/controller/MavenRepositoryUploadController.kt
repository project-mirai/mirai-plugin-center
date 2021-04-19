/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.controller

import kotlinx.coroutines.reactor.mono
import net.mamoe.mirai.plugincenter.dto.Resp
import net.mamoe.mirai.plugincenter.dto.r
import net.mamoe.mirai.plugincenter.services.PluginDescService
import net.mamoe.mirai.plugincenter.services.PluginStorageService
import net.mamoe.mirai.plugincenter.utils.isAvailable
import net.mamoe.mirai.plugincenter.utils.isOwnedBy
import net.mamoe.mirai.plugincenter.utils.loginUserOrReject
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import java.nio.file.Path
import kotlin.io.path.name

/**
 * Fake Maven repository
 */
@RestController
@RequestMapping("/v1/publish")
class MavenRepositoryUploadController(
    val desc: PluginDescService,
    val storage: PluginStorageService,
) {
    data class PublishArtifact(
        val group: String,
        val artifact: String,
        val version: String,
        val artifactName: String
    )

    companion object {
        private val PublishArtifact.pluginId: String
            get() {
                return "$group.$artifact"
            }

        private fun String.splitLatest(): Pair<String, String> {
            val index = this.lastIndexOf('/')
            return substring(0, index) to substring(index + 1)
        }

        private val ServerWebExchange.publishArtifact: PublishArtifact?
            get() {
                return Path.of(request.uri.path).asArtifact()
            }


        /**
         * 转化 api 请求路径为 [PublishArtifact] 数据
         * 格式要求：/v1/publish/upload/<group>/<artifact>/<version>/<name>
         */
        fun Path.asArtifact(): PublishArtifact? {
            val xs = this.toList().drop(3).map { it.fileName.toString() }

            return if (xs.size >= 4) {
                val (artifact, version, artifactName) = xs.takeLast(3)
                val group = xs.dropLast(3).joinToString(separator = ".")

                PublishArtifact(group, artifact, version, artifactName)
            } else {
                null
            }
        }
    }

    @GetMapping("/upload/**")
    fun doGet(exchange: ServerWebExchange): Any {
        return with(exchange.publishArtifact ?: return Resp.BAD_REQUEST) {
            val plugin = desc.get(pluginId) ?: return Resp.NOT_FOUND
            val resource = storage.get(plugin.pluginId, version, artifactName)

            if (resource.exists()) {
                resource
            } else {
                Resp.NOT_FOUND
            }
        }
    }

    @PutMapping("/upload/**")
    fun doUpload(exchange: ServerWebExchange): Any {
        val usr = exchange.loginUserOrReject

        return with(exchange.publishArtifact ?: return Resp.BAD_REQUEST) {
            if (artifactName.startsWith("maven-metadata.xml")) { // dropped
                return Resp.OK
            }
            val plugin = desc.get(pluginId) ?: return Resp.NOT_FOUND
            if (!(plugin.isOwnedBy(usr) && plugin.isAvailable())) {
                return Resp.FORBIDDEN
            }

            mono {
                storage.write(plugin.pluginId, version, artifactName, exchange.request.body)
                r.created<Any>()
            }
        }
    }
}
