/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.utils.shield

import java.util.*
import java.util.concurrent.atomic.AtomicLong
import kotlin.collections.ArrayList

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
     * This operation should be O(1)
     */
    fun put(element: T):Boolean

    companion object{
        fun <V> create(capacity:Int): CircularIndexHashSet<V> = CircularIndexHashSetImpl(capacity)
    }
}


private class CircularIndexHashSetImpl<T>(override val capacity:Int):CircularIndexHashSet<T>{


    /**
     * Identify the current index
     */
    private val curr = AtomicLong(0)

    /**
     * Use a circular index array to implement pop and push
     */
    private val array = ArrayList<T?>(initialCapacity = capacity).apply {
        repeat(capacity){
            add(null)
        }
    }

    /**
     * use a hash set with copied of data for o(1) contains check
     */
    private val set = Collections.synchronizedSet(HashSet<T>(initialCapacity = (this.capacity * 0.75 + 1).toInt()))//prevent rehash, 0.75 refers to load factor


    override fun put(element: T): Boolean {

        if(!set.add(element)){
            return false
        }

        val slot = (curr.getAndIncrement() % this.capacity).toInt()

        val bucket = array[slot]

        if(bucket != null){
            set.remove(bucket)
        }

        array[slot] = element

        return true
    }

}

