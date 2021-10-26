import {SmileOutlined, TabletOutlined, UploadOutlined} from "@ant-design/icons";
import React from "react";

const CommonSubRouters =
    {
        name: '插件中心',
        icon: <SmileOutlined />,
        needLogin: false,
        path: '/app',
    }

const DeveloperSubRouters = {
    name: '开发者',
    icon: <TabletOutlined />,
    needLogin: true,
    routes: [
        {
            name: '新建插件',
            icon: <UploadOutlined />,
            needLogin: true,
            path: '/app/create',
        }
    ]
}

export function getRouterObject(subRouters:any) {
    return {
        route: {
            routes: subRouters,
        },
        location: {
            pathname: '/',
        },
    }
}

export const GuestRouter = getRouterObject([CommonSubRouters])
export const DeveloperRouter = getRouterObject([CommonSubRouters, DeveloperSubRouters])