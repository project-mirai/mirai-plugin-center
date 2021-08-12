import React from 'react';
import { Avatar } from 'antd';
import { UserOutlined } from '@ant-design/icons';

import ProLayout from '@ant-design/pro-layout';
import defaultProps from './_defaultProps';

export default (props:any) => {
    return (
      <div
          id="app-layout"
          style={{
            height: '100vh'
          }}
      >
        <ProLayout
            {...defaultProps}
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
                <a
                    onClick={() => {
                      //setPathname(item.path || '/welcome');
                    }}
                >
                  {dom}
                </a>
            )}
            rightContentRender={() => (
                <div>
                  <Avatar shape="square" size="small" icon={<UserOutlined />} />
                </div>
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
                backgroundColor:'white'
            }}
        >
            {props.children}
        </ProLayout>
      </div>
  );
};