/*
 * Copyright 2019-2021 Mamoe Technologies and contributors.
 *
 * 此源代码的使用受 GNU AFFERO GENERAL PUBLIC LICENSE version 3 许可证的约束, 可以在以下链接找到该许可证.
 * Use of this source code is governed by the GNU AGPLv3 license that can be found through the following link.
 *
 * https://github.com/project-mirai/mirai-plugin-center/blob/master/LICENSE
 */

package net.mamoe.mirai.plugincenter.model;

import kotlinx.serialization.Serializable;

/**
 * 权限枚举类，权限代码由两部分组成：<br>
 * <ul>
 *     <li>group (more than 1 digit)：权限组代码</li>
 *     <li>permission code (2 digits)：权限代码</li>
 * </ul>
 *
 * <h2>权限代码</h2>
 *
 * <ul>
 *     <li>00: 未定义，不应该使用</li>
 *     <li>10: 完全读取权限</li>
 *     <li>1x: 不完全读取权限，用户自定义</li>
 *     <li>20: 完全写入权限（不隐式包含读取权限）</li>
 *     <li>2x: 不完全写入权限，用户自定义</li>
 * </ul>
 */
public enum PermissionEntity {
    // 0 类权限：未定义权限
    Undefined(0000),

    // 10 类权限：插件列表权限
    ReadPluginList(1010),       // 对插件列表的完全读取权限
    ReadAcceptedPluginList(1011),    // 对通过审核的插件列表的读取权限，所有新用户都应该带有这个权限

    WritePlugin(1020),          // 对插件的完全写入权限
    WriteOwnedPlugin(1021),          // 对自己创建的插件的写入权限

    // 20 类权限：管理权限
    // 21 类权限：用户列表
    ReadUserList(2110),    // 用户列表的完全读取
    WriteUserList(2120),   // 用户列表的完全写入

    // 22 类权限：Role 列表
    ReadRoleList(2210),         // Role 列表的完全读取
    WriteRoleList(2220),        // Role 列表的完全写入

    // 23 类权限：Role-Permission 列表
    ReadRolePermissionList(2310),
    WriteRolePermissionList(2320),

    ;

    private final int code;

    PermissionEntity(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
