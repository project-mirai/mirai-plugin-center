/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.utils.shield

/**
 * CircularIndexHashSet should always be thread safe
 *
 * This stores [capacity] amounts of data, it does not allow resize.
 *
 */

interface CircularIndexHashSet<T>{
    val capacity:Int

    /**
     * Putting an element into CircularIndexHashSet
     *
     * 1: If this element already presents in the hash set, return false
     * 2: add the element into the container
     * 3: If the amount of data exceed [capacity], remove the <b>oldest</b> element that have being added
     *
     * This operation should be O(N)
     */
    fun put(element: T):Boolean

    companion object{
        fun <V> create(capacity:Int): CircularIndexHashSet<V> = CircularIndexHashSetImpl(capacity)
    }
}


private class CircularIndexHashSetImpl<T>(override val capacity:Int):CircularIndexHashSet<T>{
    override fun put(element: T): Boolean {
        TODO("Not yet implemented")
    }
}

