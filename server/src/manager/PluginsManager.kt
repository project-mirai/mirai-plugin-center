/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.manager

import org.springframework.core.io.FileSystemResource
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException
import java.io.InputStream
import java.nio.file.Paths

typealias VersionCode = Int
typealias PluginId = Int

interface PluginFile {
    val version: PluginVersion
    val fileName: String
    val exists: Boolean

    /**
     * This method will [InputStream.close] the stream
     * [upload] will create a new file if current file does not exists
     */
    fun upload(`in`: MultipartFile)
    fun download(): FileSystemResource?

    /**
     * Delete this file
     * May throw some exceptions when file doesn't exist
     * IDK XD
     */
    fun delete()
}

interface PluginVersion {
    val plugin: Plugin
    val versionCode: VersionCode
    val exists: Boolean

    /**
     * note that [PluginFile] which returned may not exist
     */
    fun pluginFile(fileName: String): PluginFile

    /**
     * Delete whole version directory
     */
    fun deleteVersion()
}

interface Plugin {
    val pluginId: PluginId

    fun pluginVersion(version: VersionCode): PluginVersion

    /**
     * OH NO!
     * Delete whole plugin directory
     */
    fun deletePlugin()
}

object PluginsManager {
    fun plugin(pluginId: PluginId): Plugin {
        return Plugin(pluginId)
    }
}

data class PluginFileImpl(override val version: PluginVersion, override val fileName: String) : PluginFile {
    private val file by lazy {
        realFile
    }

    override val exists: Boolean
        get() = file.exists()

    override fun upload(`in`: MultipartFile) {
        uploadFromStream(`in`.inputStream)
    }

    // TODO: TEST ONLY
    fun uploadFromStream(`in`: InputStream) {
        if (! exists) {
            if (! file.parentFile.mkdirs()) {
                throw IOException("Failed to creating parent directories.")
            }

            if (! file.createNewFile()) {
                throw IOException("Failed to creating plugin file.")
            }
        }

        `in`.transferTo(file.outputStream())
    }

    override fun download(): FileSystemResource? {
        return if (exists) {
            FileSystemResource(file)
        } else {
            null
        }
    }

    override fun delete() {
        file.delete()
    }
}

data class PluginVersionImpl(override val plugin: Plugin, override val versionCode: VersionCode) : PluginVersion {
    private val dir by lazy {
        realDir
    }

    override val exists: Boolean
        get() = dir.exists()

    override fun pluginFile(fileName: String): PluginFile {
        return PluginFile(this, fileName)
    }

    /**
     * buggy code?
     * It require that only File under this directory
     */
    override fun deleteVersion() {
        dir.listFiles()?.forEach {
            it.delete()
        }

        dir.delete()

        TODO()
    }
}

data class PluginImpl(override val pluginId: PluginId) : Plugin {
    override fun pluginVersion(version: VersionCode): PluginVersion {
        return PluginVersion(this, version)
    }

    override fun deletePlugin() {
        TODO("Not yet implemented")
    }
}

fun PluginFile(plugin: PluginVersion, fileName: String): PluginFile {
    return PluginFileImpl(plugin, fileName)
}

fun PluginVersion(plugin: Plugin, versionCode: VersionCode): PluginVersion {
    return PluginVersionImpl(plugin, versionCode)
}

fun Plugin(pluginId: PluginId): Plugin {
    return PluginImpl(pluginId)
}

@Suppress("unused")
val PluginsManager.realDir: File
    get() {
        return Paths.get(".", "plugins").toAbsolutePath().toFile()
    }

val Plugin.realDir: File
    get() {
        return PluginsManager.realDir.resolve(this.pluginId.toString())
    }

val PluginVersion.realDir: File
    get() {
        return this.plugin.realDir.resolve(this.versionCode.toString())
    }

val PluginFile.realFile: File
    get() {
        return this.version.realDir.resolve(this.fileName)
    }