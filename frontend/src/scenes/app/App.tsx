import React from 'react';
import {Avatar, Button, Dropdown, Menu, Space} from 'antd';
import { UserOutlined } from '@ant-design/icons';

import ProLayout from '@ant-design/pro-layout';
import userRouter from './router/UserRouter';
import guestRouter from './router/GuestRouter';
import axios from "axios";
import {useHistory} from "react-router";



export default (props:any) => {
    const defaultUserInfo = {
        nick:'nickname'
    }
    const [logon, setLogon] = React.useState(false)
    const [userInfo, setUserInfo] = React.useState(defaultUserInfo)
    const history = useHistory()
    const loadingInfo = () => {
        axios.get('/v1/sso/whoami').then((res)=>{
            setLogon(true)
            setUserInfo(res.data.response)
        }).catch(()=>{
            setLogon(false)

            setUserInfo(defaultUserInfo)
        })
    }
    React.useEffect(()=>loadingInfo(),[])
    const userControlMenu = (
        <Menu>
            {logon?
                <>
                    <Menu.Item>
                        <a>
                            {userInfo.nick}
                        </a>
                    </Menu.Item>
                    <Menu.Item onClick={()=>history.push('/verify/resetpassword/manual/')}>
                            修改密码
                    </Menu.Item>
                    <Menu.Item danger>注销</Menu.Item>
                </>
                :
                <>
                    <Menu.Item>
                        <a rel="noopener noreferrer" onClick={()=>history.push('/verify/login')}>
                            请登录
                        </a>
                    </Menu.Item>
                </>
            }
        </Menu>
    );
    return (
        <div
            id="app-layout"
            style={{
                height: '100vh'
            }}
        >
            <ProLayout
                {...(logon?userRouter:guestRouter)}
                waterMarkProps={{
                    content: 'Mirai',
                }}
                menuFooterRender={(props) => {
                    return (
                        <a
                            style={{
                                lineHeight: '48rpx',
                                display: 'flex',
                                height: 48,
                                color: 'rgba(255, 255, 255, 0.65)',
                                alignItems: 'center',
                            }}
                            href="https://preview.pro.ant.design/dashboard/analysis"
                            target="_blank"
                            rel="noreferrer"
                        >
                            <img
                                alt="pro-logo"
                                src="https://procomponents.ant.design/favicon.ico"
                                style={{
                                    width: 16,
                                    height: 16,
                                    margin: '0 16px',
                                    marginRight: 10,
                                }}
                            />
                            {!props?.collapsed && 'project-mirai'}
                        </a>
                    );
                }}
                onMenuHeaderClick={(e) => console.log(e)}
                menuItemRender={(item, dom) => (
                    <a aria-disabled={!logon}
                        onClick={() => {
                            history.push(item.path as string,true)
                        }}
                    >
                        {dom}
                    </a>
                )}
                rightContentRender={() => (
                    <Space wrap>
                        {
                            logon ?
                                (<Dropdown overlay={userControlMenu}>
                                    <a className="ant-dropdown-link" onClick={e => e.preventDefault()}>
                                        <Avatar shape="square" size="small" icon={<UserOutlined/>}/>
                                    </a>
                                </Dropdown>)
                                :
                                (<Button onClick={() => history.push("/verify/login")}>请登录</Button>)
                        }

                    </Space>
                )}
                title={'Mirai插件中心'}
                logo={false}
                fixSiderbar={true}
                navTheme={'dark'}
                layout={'top'}
                contentWidth={'Fixed'}
                splitMenus={false}
                fixedHeader={false}
                style={{
                    backgroundColor: 'white'
                }}
            >
                {props.children}
            </ProLayout>
        </div>
    );
};