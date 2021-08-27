import React from 'react';
import {SmileOutlined, CrownOutlined, TabletOutlined, AntDesignOutlined, UploadOutlined} from '@ant-design/icons';

export default {
    route: {
        routes: [
            {
                name: '插件中心',
                icon: <SmileOutlined />,
                path: '/app',
            },
            {
                name: '开发者',
                icon: <TabletOutlined />,
                routes: [
                    {
                        name: '新建插件',
                        icon: <UploadOutlined />,
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