// 创建一个独立的axios实例
import axios from "axios";
import {message} from "antd";

export const service = axios.create({
    baseURL: process.env.REACT_APP_API_URL,
    timeout: 10000,
    withCredentials: true
});

service.interceptors.response.use((response)=>{
    return response
},(err)=>{
    message.error(err.response.data.message)
    throw err
});

export default service;