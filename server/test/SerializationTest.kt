package net.mamoe.mirai.plugincenter

import net.mamoe.mirai.plugincenter.dto.PluginDesc
import net.mamoe.mirai.plugincenter.utils.toJsonUseJackson
import org.junit.jupiter.api.Test

class SerializationTest {

    @Test
    fun serializationUsingKts() {
        val desc = PluginDesc("id", "name")

        val json = desc.toJsonUseJackson()
        assert(json.contains("\"id\"")) { json }
    }
}