/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.model;

import net.mamoe.mirai.plugincenter.model.interfaces.Logable;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "role", schema = "public", catalog = "plugins")
public class RoleEntity implements Logable {
    private int id;
    private String name;
    private LogEntity log;
    private Collection<RolePermissionEntity> permissionSet;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @OneToMany(mappedBy = "id")
    public Collection<RolePermissionEntity> getPermissionSet() {
        return permissionSet;
    }

    public void setPermissionSet(Collection<RolePermissionEntity> permissionSet) {
        this.permissionSet = permissionSet;
    }

    @OneToOne
    @JoinColumn(name = "log")
    public LogEntity getLog() {
        return log;
    }

    public void setLog(LogEntity log) {
        this.log = log;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity that = (RoleEntity) o;
        return id == that.id && name.equals(that.name) && permissionSet.equals(that.permissionSet) && Objects.equals(log, that.log);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, permissionSet, log);
    }

    @Override
    public LogEntity getLogChain() {
        return getLog();
    }

    @Override
    public void setLogChain(LogEntity log) {
        setLog(log);
    }
}
