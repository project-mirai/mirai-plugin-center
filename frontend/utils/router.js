export const PERMISSION_NO_LOGIN = 1
export const PERMISSION_NORMAL_USR = 2
export const PERMISSION_DEV = 3
export const PERMISSION_MONITOR = 4
export const PERMISSION_ADMINISTRATOR = 5

/**
 * Router对象
 * @param {string} name
 * @param {string} icon
 * @param {string} link
 */
export function RouterObject(name,icon,link) {
  this.name = name
  this.icon = icon
  this.link = link
}

const ROUTER_INDEX = new RouterObject('主页','mdi-home','/');
const ROUTER_UPLOAD = new RouterObject('上传插件','mdi-upload','/upload');
const ROUTER_AUDIT = new RouterObject('审核','','/audit');
/**
 * 获取当前的用户权限
 * @returns {number}
 */
export function getNowPermission() {
  return PERMISSION_DEV
}

/**
 * 获取不同权限的用户router
 * @param permission
 * @returns {*[]}
 */
export function getRouter(permission) {
  switch (permission) {
    case PERMISSION_NORMAL_USR:
      return getRouter(PERMISSION_NO_LOGIN).concat([])
    case PERMISSION_DEV:
      return getRouter(PERMISSION_NORMAL_USR).concat([ROUTER_UPLOAD])
    case PERMISSION_MONITOR:
      return getRouter(PERMISSION_DEV).concat([ROUTER_AUDIT])
    case PERMISSION_ADMINISTRATOR:
      return getRouter(PERMISSION_DEV).concat([])
    default:
      return [ROUTER_INDEX]
  }
}

export function getSelfRouter() {
  return getRouter(getNowPermission());
}
