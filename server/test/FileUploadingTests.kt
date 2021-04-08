package net.mamoe.mirai.plugincenter

import net.mamoe.mirai.plugincenter.manager.PluginFileImpl
import net.mamoe.mirai.plugincenter.manager.PluginsManager
import org.junit.jupiter.api.Test
import java.io.File

class FileUploadingTests {
    @Test
    fun uploadFile() {
        val file = PluginsManager
            .plugin(114514)
            .pluginVersion(1919810)
            .pluginFile("黄色.sabee") as PluginFileImpl

        file.uploadFromStream(File("./build.gradle.kts").inputStream())
        file.uploadFromStream(File("./build.gradle.kts").inputStream())     // upload twice
    }

    @Test
    fun downloadFile() {
        val file = PluginsManager
            .plugin(114514)
            .pluginVersion(1919810)
            .pluginFile("黄色.sabee")

        file.download()?.inputStream?.transferTo(System.out) ?: println("file not found")
    }
}