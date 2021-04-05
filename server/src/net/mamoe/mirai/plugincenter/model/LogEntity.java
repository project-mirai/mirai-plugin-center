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
import java.util.Objects;

@Entity
@Table(name = "log", schema = "public", catalog = "plugins")
public class LogEntity {
    private long id;
    private Integer operator;
    private String msg;
    private String otherInfo;
    private UserEntity userByOperator;

    @Id
    @Column(name = "id")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "operator")
    public Integer getOperator() {
        return operator;
    }

    public void setOperator(Integer operator) {
        this.operator = operator;
    }

    @Basic
    @Column(name = "msg")
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Basic
    @Column(name = "other_info")
    public String getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LogEntity logEntity = (LogEntity) o;
        return id == logEntity.id && Objects.equals(operator, logEntity.operator) && Objects.equals(msg, logEntity.msg) && Objects.equals(otherInfo, logEntity.otherInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, operator, msg, otherInfo);
    }

    @ManyToOne
    @JoinColumn(name = "operator", referencedColumnName = "uid",insertable = false,updatable = false)
    public UserEntity getUserByOperator() {
        return userByOperator;
    }

    public void setUserByOperator(UserEntity userByOperator) {
        this.userByOperator = userByOperator;
    }
}
