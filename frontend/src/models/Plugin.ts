import {UserInfo} from "./User";

/**
 * 插件信息
 */
export interface PluginInfo {
    id:string
    info:string
    name:string
    owner:UserInfo
    status:string
}

export interface BasicPluginInfo {
    id: string
    info: string
    name: string
}