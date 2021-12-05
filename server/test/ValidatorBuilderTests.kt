package net.mamoe.mirai.plugincenter

import net.mamoe.mirai.plugincenter.utils.validate.requires
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ValidatorBuilderTests {
    @Test
    fun stringValidator0() {
        requires {
            "123" matches Regex("\\d+")
            "123" matches "\\d+"
        }

        requires {
            +requires("123") {
                +match("\\d+")
            }
        }
    }

    @Test
    fun stringValidator1() {
        assertThrows<IllegalArgumentException> {
            requires {
                "123" matches Regex("[a-z]")
            }
        }

        assertThrows<IllegalArgumentException> {
            requires {
                +requires("123") {
                    +match("[a-z]")
                }
            }
        }
    }
}