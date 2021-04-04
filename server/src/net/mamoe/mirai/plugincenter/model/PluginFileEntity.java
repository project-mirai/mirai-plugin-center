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
import java.util.Objects;

@Entity
@Table(name = "plugin_file", schema = "public", catalog = "plugins")
public class PluginFileEntity {
    private int id;
    private String version;
    private int versionCode;
    private int pluginId;
    private int fileId;
    private Timestamp publishTime;
    private String platform;
    private PluginEntity pluginByPluginId;
    private FileEntity fileByFileId;

    @Id
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "version")
    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Basic
    @Column(name = "version_code")
    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Basic
    @Column(name = "plugin_id")
    public int getPluginId() {
        return pluginId;
    }

    public void setPluginId(int pluginId) {
        this.pluginId = pluginId;
    }

    @Basic
    @Column(name = "file_id")
    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
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
    @Column(name = "platform")
    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PluginFileEntity that = (PluginFileEntity) o;
        return id == that.id && versionCode == that.versionCode && pluginId == that.pluginId && fileId == that.fileId && Objects.equals(version, that.version) && Objects.equals(publishTime, that.publishTime) && Objects.equals(platform, that.platform);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, versionCode, pluginId, fileId, publishTime, platform);
    }

    @ManyToOne
    @JoinColumn(name = "plugin_id", referencedColumnName = "id", nullable = false,insertable = false ,updatable = false)
    public PluginEntity getPluginByPluginId() {
        return pluginByPluginId;
    }

    public void setPluginByPluginId(PluginEntity pluginByPluginId) {
        this.pluginByPluginId = pluginByPluginId;
    }

    @ManyToOne
    @JoinColumn(name = "file_id", referencedColumnName = "id", nullable = false,insertable = false ,updatable = false)
    public FileEntity getFileByFileId() {
        return fileByFileId;
    }

    public void setFileByFileId(FileEntity fileByFileId) {
        this.fileByFileId = fileByFileId;
    }
}
