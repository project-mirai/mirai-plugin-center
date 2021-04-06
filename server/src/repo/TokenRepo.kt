/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.repo

import net.mamoe.mirai.plugincenter.model.TokenEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.Repository
import java.sql.Timestamp
import javax.transaction.Transactional

interface TokenRepo : Repository<TokenEntity, Int> {
    fun findTokenEntityById(id: Int): TokenEntity?
    fun findTokenEntityByToken(token: String): TokenEntity?

    @Modifying
    @Transactional
    @Query("""INSERT INTO "public"."token"(token, owner, add_time, expire_time) VALUES (?1, ?2, ?3, ?4)""", nativeQuery = true)
    fun newToken(token: String, owner: Int, addTime: Timestamp, expireTime: Timestamp): Int

}
