import React from 'react';
import {SmileOutlined, TabletOutlined, UploadOutlined} from '@ant-design/icons';

export default {
    route: {
        routes: [
            {
                name: '插件中心',
                icon: <SmileOutlined />,
                needLogin: false,
                path: '/app',
            },
            {
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
        ],
    },
    location: {
        pathname: '/',
    },
};