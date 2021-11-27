import {LOGIN_ACCOUNT, LOGOUT_ACCOUNT} from "./accountActionTypes";
import {UserInfo} from "../../models/User";

export const getLoginAccountAction = (value: UserInfo) => {
    return {
        type: LOGIN_ACCOUNT,
        value
    }
}
export const getLogoutAction = () => {
    return {
        type: LOGOUT_ACCOUNT
    }
}