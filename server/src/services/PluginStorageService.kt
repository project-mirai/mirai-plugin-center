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
import net.mamoe.mirai.plugincenter.utils.useBIO
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Service
class PluginDescService(
    private val repo: PluginRepo
) {
    fun getAcceptedList(page: Int): List<PluginEntity> {
        require(page >= 0) { "Page invalid: '$page'. Should be at least 0." }
        return repo.findAllByRawState(PluginEntity.Status.Accepted.ordinal ,PageRequest.of(page,20)).toList()
    }

    fun getList(page: Int): List<PluginEntity> {
        require( page >= 0 ) {  }
        return repo.findAll(PageRequest.of(page, 20)).toList()
    }

    fun get(pid: String): PluginEntity? = repo.findByPluginId(pid)

    fun count() = repo.count()

    fun countByRawStates(rawState:Int):Long {
        return repo.findAllByRawState(rawState ,PageRequest.of(0,20)).totalElements
    }

    fun update(plugin: PluginEntity, apply: PluginEntity.() -> Unit): PluginEntity {
        return repo.save(plugin.apply(apply))
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
    fun resolvePluginDir(pid: String) = storage.resolve(pid)

    fun get(plugin: PluginEntity, version: String, filename: String) = FileSystemResource(resolveFile(plugin.pluginId, version, filename))
    fun get(pid: String, version: String, filename: String) = FileSystemResource(resolveFile(pid, version, filename))

    suspend fun write(pid: String, version: String, filename: String, data: Flux<DataBuffer>) {
        val file = get(pid, version, filename)
        file.file.parentFile?.mkdirs()
        try {
            runBIO { file.outputStream.buffered() }.use { output ->
                val bufferByteArray = ByteArray(DEFAULT_BUFFER_SIZE)
                data.asFlow().collect { data ->
                    data.asInputStream(true).useBIO { input ->
                        input.copyToBuffered(output, bufferByteArray)
                    }
                }
            }
            // TODO: plugin_file update
        } catch (e: Exception) {
            file.file.delete()
            throw e
        }
    }

    fun hasVersion(pid: String, version: String): Boolean {
        return resolveVersionDir(pid, version).exists()
    }

    fun getVersionFiles(pid: String, version: String): Array<String> {
        return resolveVersionDir(pid, version).list()?: arrayOf()
    }

    fun getVersionList(pid: String): Array<String> {
        return resolvePluginDir(pid).list()?: arrayOf()
    }

    fun addVersion(pid: String, version: String): Boolean {
        return resolveVersionDir(pid, version).mkdirs()
    }

    fun delete(pid: String, version: String): Boolean {
        val dir = resolveVersionDir(pid, version)
        if (!dir.exists()) return false
        return dir.deleteRecursively()
    }

    fun delete(pid: String, version: String, fileName:String): Boolean {
        val file = resolveFile(pid, version,fileName)
        if (!file.exists()) return false
        return file.delete()
    }

    private fun InputStream.copyToBuffered(out: OutputStream, buffer: ByteArray): Long {
        var bytesCopied: Long = 0
        var bytes = read(buffer)
        while (bytes >= 0) {
            out.write(buffer, 0, bytes)
            bytesCopied += bytes
            bytes = read(buffer)
        }
        return bytesCopied
    }
}

fun PluginDescService.update(pid: String, apply: PluginEntity.() -> Unit): PluginEntity? {
    val existing = this.get(pid) ?: return null
    return this.update(existing, apply)
}

fun PluginDescService.updateOrDefault(pid: String, default: PluginEntity, apply: PluginEntity.() -> Unit): PluginEntity {
    val existing = this.get(pid)
    return this.update(existing ?: default, apply)
}
