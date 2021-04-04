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
@Table(name = "plugin", schema = "public", catalog = "plugins")
public class PluginEntity {
    private int id;
    private String name;
    private int owner;
    private String packageId;
    private float rank;
    private Object publishTime;
    private String desc;
    private boolean banned;
    private Object updateTime;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    @Column(name = "package_id")
    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    @Basic
    @Column(name = "rank")
    public float getRank() {
        return rank;
    }

    public void setRank(float rank) {
        this.rank = rank;
    }

    @Basic
    @Column(name = "publish_time")
    public Object getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Object publishTime) {
        this.publishTime = publishTime;
    }

    @Basic
    @Column(name = "desc")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Basic
    @Column(name = "banned")
    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    @Basic
    @Column(name = "update_time")
    public Object getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Object updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginEntity that = (PluginEntity) o;
        return id == that.id && owner == that.owner && Float.compare(that.rank, rank) == 0 && banned == that.banned && Objects.equals(name, that.name) && Objects.equals(packageId, that.packageId) && Objects.equals(publishTime, that.publishTime) && Objects.equals(desc, that.desc) && Objects.equals(updateTime, that.updateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner, packageId, rank, publishTime, desc, banned, updateTime);
    }
}
