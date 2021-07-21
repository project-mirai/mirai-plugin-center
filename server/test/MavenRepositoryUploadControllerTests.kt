package net.mamoe.mirai.plugincenter

import org.junit.jupiter.api.Test
import java.nio.file.Path
import net.mamoe.mirai.plugincenter.controller.MavenRepositoryUploadController.PublishArtifact
import net.mamoe.mirai.plugincenter.controller.MavenRepositoryUploadController.Companion.asArtifact

class MavenRepositoryUploadControllerTests {
    @Test
    fun asArtifactTest() {
        val path = Path.of("v1", "publish", "upload", "net", "mamoe", "mirai", "mirai-core", "1.1.4", "mirai-core-1.1.4-all.jar")

        assert(path.subpath(0, 3).asArtifact() == null)
        assert(path.subpath(0, 4).asArtifact() == null)
        assert(path.subpath(0, 5).asArtifact() == null)
        assert(path.subpath(0, 6).asArtifact() == null)
        assert(path.subpath(0, 7).asArtifact() == PublishArtifact("net", "mamoe", "mirai", "mirai-core"))
        assert(path.subpath(0, 8).asArtifact() == PublishArtifact("net.mamoe", "mirai", "mirai-core", "1.1.4"))
        assert(path.subpath(0, 9).asArtifact() == PublishArtifact("net.mamoe.mirai",  "mirai-core", "1.1.4", "mirai-core-1.1.4-all.jar"))
    }
}