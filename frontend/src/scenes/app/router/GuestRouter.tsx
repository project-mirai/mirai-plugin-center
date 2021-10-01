import React from 'react';
import {SmileOutlined} from '@ant-design/icons';

export default {
    route: {
        routes: [
            {
                name: '插件中心',
                icon: <SmileOutlined />,
                needLogin: false,
                path: '/app',
            }
        ],
    },
    location: {
        pathname: '/',
    },
};