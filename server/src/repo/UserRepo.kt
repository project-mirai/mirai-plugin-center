/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.repo

import net.mamoe.mirai.plugincenter.model.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.QueryHints
import java.sql.Timestamp
import javax.persistence.QueryHint
import javax.transaction.Transactional

interface UserRepo : JpaRepository<UserEntity, Int> {
    fun findUserEntityByEmail(email: String): UserEntity?

    fun findByUid(uid: Int): UserEntity?
    fun existsByEmail(email: String): Boolean

//    @Modifying
//    @Transactional
//    @Query("""INSERT INTO public."user"(nick, email, password, register_ip, role,register_time) VALUES (?1,?2,?3,?4,?5,?6)""", nativeQuery = true)
//    fun registerUser(nick: String, email: String, password: String, registerIp: String, role: Int, registerTime: Timestamp): Int
}

fun UserEntity.toStringGitLike() = "$nick <$email>"