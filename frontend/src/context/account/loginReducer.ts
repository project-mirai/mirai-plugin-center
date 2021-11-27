import {LOGIN_ACCOUNT, LOGOUT_ACCOUNT} from "./accountActionTypes";
import {UserInfo} from "../../models/User";

interface AccountState {
    inputValue?:UserInfo,
    needLogin: boolean
}

const defaultState: AccountState = {
    inputValue: undefined,
    needLogin: true
}

export default (state: AccountState = defaultState, action: any) => {
    const newState: AccountState = JSON.parse(JSON.stringify(state));
    if (action.type === LOGIN_ACCOUNT) {
        newState.inputValue = action.value;
        newState.needLogin = false
        return newState;
    }
    if (action.type === LOGOUT_ACCOUNT) {
        newState.needLogin = true
        return newState;
    }
    return state;
}