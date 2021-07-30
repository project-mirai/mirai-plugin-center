import {UserInfo} from "./User";

/**
 * 插件状态
 */
export enum PluginStatus {
    Accepted,
    Denied
}

/**
 * 插件信息
 */
export interface PluginInfo {
    id:string
    info:string
    name:string
    owner:UserInfo
    status:PluginStatus
}