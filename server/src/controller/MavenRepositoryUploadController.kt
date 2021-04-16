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

/**
 * Fake Maven repository
 */
@RestController
@RequestMapping("/v1/publish")
class MavenRepositoryUploadController(
    val desc: PluginDescService,
    val storage: PluginStorageService,
) {
    private data class PublishArtifact(
        val group: String,
        val artifact: String,
        val version: String,
        val artifactName: String
    )

    private fun String.splitLatest(): Pair<String, String> {
        val index = this.lastIndexOf('/')
        return substring(0, index) to substring(index + 1)
    }

    private fun String.doSplitPath(): PublishArtifact {
        val p = this
            .removePrefix("/")
            .removePrefix("v1/publish/upload/")

        val (tmp0, artifactName) = p.splitLatest()
        val (tmp1, version) = tmp0.splitLatest()
        val (group, artifact) = tmp1.splitLatest()

        return PublishArtifact(group.replace('/', '.'), artifact, version, artifactName)
    }

    @GetMapping("/upload/**")
    fun doGet(exchange: ServerWebExchange): Any {
        // val (group, artifact, version, artifactName) = exchange.request.uri.path.doSplitPath()

        return Resp.NOT_FOUND
    }

    @PutMapping("/upload/**")
    fun doUpload(exchange: ServerWebExchange): Any {
        val usr = exchange.loginUserOrReject
        val (group, artifact, version, artifactName) = exchange.request.uri.path.doSplitPath()
        val pid = "$group.$artifact"
        if (artifactName.startsWith("maven-metadata.xml")) { // dropped
            return Resp.OK
        }
        val plugin = desc.get(pid) ?: return Resp.NOT_FOUND
        if (! (plugin.isOwnedBy(usr) && plugin.isAvailable())) {
            return Resp.FORBIDDEN
        }
        return mono {
            storage.write(plugin.pluginId, version, artifactName, exchange.request.body)
            r.created<Any>()
        }
    }
}
