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
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "plugin", schema = "public", catalog = "plugins")
public class PluginEntity {
    private int id;
    private String name;
    private int owner;
    private String packageId;
    private float rank;
    private Timestamp publishTime;
    private String desc;
    private Timestamp updateTime;
    private String postUrl;
    private int status;
    private UserEntity userByOwner;
    private Collection<PluginFileEntity> pluginFilesById;

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
    public Timestamp getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Timestamp publishTime) {
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
    @Column(name = "update_time")
    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    @Basic
    @Column(name = "post_url")
    public String getPostUrl() {
        return postUrl;
    }

    public void setPostUrl(String postUrl) {
        this.postUrl = postUrl;
    }

    @Basic
    @Column(name = "status")
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginEntity that = (PluginEntity) o;
        return id == that.id && owner == that.owner && Float.compare(that.rank, rank) == 0 && status == that.status && Objects.equals(name, that.name) && Objects.equals(packageId, that.packageId) && Objects.equals(publishTime, that.publishTime) && Objects.equals(desc, that.desc) && Objects.equals(updateTime, that.updateTime) && Objects.equals(postUrl, that.postUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, owner, packageId, rank, publishTime, desc, updateTime, postUrl, status);
    }

    @ManyToOne
    @JoinColumn(name = "owner", referencedColumnName = "uid", nullable = false,insertable = false,updatable = false)
    public UserEntity getUserByOwner() {
        return userByOwner;
    }

    public void setUserByOwner(UserEntity userByOwner) {
        this.userByOwner = userByOwner;
    }

    @OneToMany(mappedBy = "pluginByPluginId")
    public Collection<PluginFileEntity> getPluginFilesById() {
        return pluginFilesById;
    }

    public void setPluginFilesById(Collection<PluginFileEntity> pluginFilesById) {
        this.pluginFilesById = pluginFilesById;
    }
}
