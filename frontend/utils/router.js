export const PERMISSION_NO_LOGIN = 1
export const PERMISSION_NORMAL_USR = 2
export const PERMISSION_DEV = 3
export const PERMISSION_MONITOR = 4
export const PERMISSION_ADMINISTRATOR = 5

/**
 * 获取当前的用户权限
 * @returns {number}
 */
export function getNowPermission() {
  return PERMISSION_NO_LOGIN
}

/**
 * 获取不同权限的用户router
 * @param permission
 * @returns {*[]}
 */
export function getRouter(permission) {
  switch (permission) {
    case PERMISSION_NORMAL_USR:
      return [].concat(getRouter(PERMISSION_NO_LOGIN))
    case PERMISSION_DEV:
      return [].concat(getRouter(PERMISSION_NORMAL_USR))
    case PERMISSION_MONITOR:
      return [].concat(getRouter(PERMISSION_DEV))
    case PERMISSION_ADMINISTRATOR:
      return [].concat(getRouter(PERMISSION_DEV))
    default:
      return []
  }
}
