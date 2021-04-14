package net.mamoe.mirai.plugincenter

import net.mamoe.mirai.plugincenter.utils.shield.CircularIndexHashSet
import org.junit.jupiter.api.Test

class ShieldTest {

    @Test
    fun testHashSet(){
        val set = CircularIndexHashSet.create<Int>(100)
        repeat(50) {
            set.put(it)
        }
        assert(!set.put(10))
        assert(!set.put(14))
        assert(!set.put(20))
        assert(!set.put(30))
        assert(!set.put(40))
        repeat(51) {
            set.put(it + 50)
        }
        assert(set.put(0))
    }





}