/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.services

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.reactive.asFlow
import net.mamoe.mirai.plugincenter.model.PluginEntity
import net.mamoe.mirai.plugincenter.repo.PluginRepo
import net.mamoe.mirai.plugincenter.utils.runBIO
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileSystemResource
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.File
import java.nio.ByteBuffer

@Service
class PluginDescService(
    private val repo: PluginRepo
) {
    fun getList(page: Int): List<PluginEntity> {
        require(page >= 0) { "Page invalid: '$page'. Should be at least 0." }
        return repo.findPluginEntitiesByIdBetween(page * 20 + 1, (page + 1) * 20)
    }

    fun get(pid: String): PluginEntity? = repo.findPluginEntityByPluginId(pid)

    fun update(pid: String, apply: PluginEntity.() -> Unit): PluginEntity {
        val existing = repo.findPluginEntityByPluginId(pid)
        return repo.save((existing ?: PluginEntity()).apply(apply))
    }

    fun delete(pid: String) {
        return repo.deletePluginEntityByPluginId(pid)
    }
}

@Service
class PluginStorageService {
    @Value("\${mirai.service.plugins.storage}")
    private lateinit var storage: File

    fun resolveFile(pid: String, version: String, filename: String) = resolveVersionDir(pid, version).resolve(filename)
    fun resolveVersionDir(pid: String, version: String) = storage.resolve(pid).resolve(version)

    fun get(plugin: PluginEntity, version: String, filename: String) = FileSystemResource(resolveFile(plugin.pluginId, version, filename))
    fun get(pid: String, version: String, filename: String) = FileSystemResource(resolveFile(pid, version, filename))

    suspend fun write(pid: String, version: String, filename: String, data: Flux<ByteBuffer>) {
        val file = get(pid, version, filename)
        file.file.parentFile?.mkdirs()
        try {
            runBIO { file.writableChannel() }.use { channel ->
                data.asFlow().collect { buffer ->
                    runBIO { channel.write(buffer) }
                }
            }
        } catch (e: Exception) {
            file.file.delete()
            throw e
        }
    }

    fun hasVersion(pid: String, version: String): Boolean {
        return resolveVersionDir(pid, version).exists()
    }

    fun delete(pid: String, version: String): Boolean {
        val dir = resolveVersionDir(pid, version)
        if (!dir.exists()) return false
        return dir.deleteRecursively()
    }
}