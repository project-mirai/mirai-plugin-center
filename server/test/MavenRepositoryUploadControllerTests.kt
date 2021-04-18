package net.mamoe.mirai.plugincenter

import org.junit.jupiter.api.Test
import java.nio.file.Path
import net.mamoe.mirai.plugincenter.controller.MavenRepositoryUploadController
import net.mamoe.mirai.plugincenter.controller.MavenRepositoryUploadController.Companion.asArtifact

class MavenRepositoryUploadControllerTests {
    @Test
    fun asArtifactTest() {
        val path = Path.of("v1", "publish", "upload", "net", "mamoe", "mirai", "mirai-core", "1.1.4", "mirai-core-1.1.4-all.jar")

        assert(path.asArtifact() ==
            MavenRepositoryUploadController.PublishArtifact(
                "net.mamoe.mirai",
                "mirai-core",
                "1.1.4",
                "mirai-core-1.1.4-all.jar"
            )
        )
    }
}