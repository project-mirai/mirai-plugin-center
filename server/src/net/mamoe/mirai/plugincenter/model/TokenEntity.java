/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.model;

import javax.persistence.*;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "token", schema = "public", catalog = "plugins")
public class TokenEntity {
    private int id;
    private String token;
    private int owner;
    private Timestamp addTime;
    private Timestamp expireTime;
    private UserEntity userByOwner;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Basic
    @Column(name = "owner")
    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    @Basic
    @Column(name = "add_time")
    public Timestamp getAddTime() {
        return addTime;
    }

    public void setAddTime(Timestamp addTime) {
        this.addTime = addTime;
    }

    @Basic
    @Column(name = "expire_time")
    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenEntity that = (TokenEntity) o;
        return id == that.id && owner == that.owner && Objects.equals(token, that.token) && Objects.equals(addTime, that.addTime) && Objects.equals(expireTime, that.expireTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token, owner, addTime, expireTime);
    }

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "uid", nullable = false,insertable = false ,updatable = false)
    public UserEntity getUserByOwner() {
        return userByOwner;
    }

    public void setUserByOwner(UserEntity userByOwner) {
        this.userByOwner = userByOwner;
    }
}
