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
@Table(name = "user", schema = "public", catalog = "plugins")
public class UserEntity {
    private int uid;
    private String nick;
    private String email;
    private String password;
    private Timestamp registerTime;
    private Timestamp lastLoginTime;
    private String registerIp;
    private String lastLoginIp;
    private boolean banned;
    private int role;
    private Collection<FileEntity> filesByUid;
    private Collection<LogEntity> logsByUid;
    private Collection<PluginEntity> pluginsByUid;
    private Collection<TokenEntity> tokensByUid;

    @Id
    @Column(name = "uid")
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    @Basic
    @Column(name = "nick")
    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "register_time")
    public Timestamp getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Timestamp registerTime) {
        this.registerTime = registerTime;
    }

    @Basic
    @Column(name = "last_login_time")
    public Timestamp getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Timestamp lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Basic
    @Column(name = "register_ip")
    public String getRegisterIp() {
        return registerIp;
    }

    public void setRegisterIp(String registerIp) {
        this.registerIp = registerIp;
    }

    @Basic
    @Column(name = "last_login_ip")
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
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
    @Column(name = "role")
    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return uid == that.uid && banned == that.banned && role == that.role && Objects.equals(nick, that.nick) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(registerTime, that.registerTime) && Objects.equals(lastLoginTime, that.lastLoginTime) && Objects.equals(registerIp, that.registerIp) && Objects.equals(lastLoginIp, that.lastLoginIp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, nick, email, password, registerTime, lastLoginTime, registerIp, lastLoginIp, banned, role);
    }

    @OneToMany(mappedBy = "userByOwner")
    public Collection<FileEntity> getFilesByUid() {
        return filesByUid;
    }

    public void setFilesByUid(Collection<FileEntity> filesByUid) {
        this.filesByUid = filesByUid;
    }

    @OneToMany(mappedBy = "userByOperator")
    public Collection<LogEntity> getLogsByUid() {
        return logsByUid;
    }

    public void setLogsByUid(Collection<LogEntity> logsByUid) {
        this.logsByUid = logsByUid;
    }

    @OneToMany(mappedBy = "userByOwner")
    public Collection<PluginEntity> getPluginsByUid() {
        return pluginsByUid;
    }

    public void setPluginsByUid(Collection<PluginEntity> pluginsByUid) {
        this.pluginsByUid = pluginsByUid;
    }

    @OneToMany(mappedBy = "userByOwner")
    public Collection<TokenEntity> getTokensByUid() {
        return tokensByUid;
    }

    public void setTokensByUid(Collection<TokenEntity> tokensByUid) {
        this.tokensByUid = tokensByUid;
    }
}
