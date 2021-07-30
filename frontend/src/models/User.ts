/**
 * 用户信息
 */
export interface UserInfo {
    email:string
    nick:string
}

/**
 * 登陆信息
 */
export interface LoginForm {
    email:string
    password:string
}

/**
 * 邮箱密码重置信息
 */
export interface ResetInfoByEmailForm {
    email:string
    password:string
    token:string
}

/**
 * 邮箱密码重置信息
 */
export interface ChangePasswordForm {
    email:string
    password:string
    newPassword:string
}

/**
 * 注册信息
 */
export interface RegisterForm {
    email:string
    nick:string
    password:string
}