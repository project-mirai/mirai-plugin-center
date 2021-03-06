/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.model;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import kotlinx.serialization.json.JsonElement;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.jetbrains.annotations.Nullable;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "log", schema = "public", catalog = "plugins")
@TypeDefs(@TypeDef(name="jsonb",typeClass = JsonBinaryType.class))
public class LogEntity {
    private long id;
    private Integer operator;
    private String msg;
    private Map<String,Object> otherInfo;
    private UserEntity userByOperator;
    private Timestamp logTime;

    // Note that this field is nullable
    private LogEntity parent;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 自增
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
    @Column(name = "log_time")
    public Timestamp getLogTime() {
        return logTime;
    }

    public void setLogTime(Timestamp logTime) {
        this.logTime = logTime;
    }

    /**
     * (Nullable) 获取日志历史的上一个记录
     */
    @Nullable
    @OneToOne()
    @JoinColumn(name = "parent_id")
    public LogEntity getParent() {
        return parent;
    }

    public void setParent(LogEntity parent) {
        this.parent = parent;
    }

    @Type(type ="jsonb")
    @Column(name = "other_info")
    public Map<String,Object> getOtherInfo() {
        return otherInfo;
    }

    public void setOtherInfo(Map<String,Object> otherInfo) {
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
