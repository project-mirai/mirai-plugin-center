import React from 'react';
import { Button, Descriptions, Result, Avatar, Space, Statistic } from 'antd';
import { LikeOutlined, UserOutlined } from '@ant-design/icons';

import ProLayout, { PageContainer } from '@ant-design/pro-layout';
import defaultProps from './_defaultProps';

const content = (
    <Descriptions size="small" column={2}>
      <Descriptions.Item label="创建人">张三</Descriptions.Item>
      <Descriptions.Item label="联系方式">
        <a>421421</a>
      </Descriptions.Item>
      <Descriptions.Item label="创建时间">2017-01-10</Descriptions.Item>
      <Descriptions.Item label="更新时间">2017-10-10</Descriptions.Item>
      <Descriptions.Item label="备注">中国浙江省杭州市西湖区古翠路</Descriptions.Item>
    </Descriptions>
);

export default () => {
    return (
      <div
          id="test-pro-layout"
          style={{
            height: '100vh',
          }}
      >
        <ProLayout
            {...defaultProps}
            waterMarkProps={{
              content: 'Pro Layout',
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
        >
          <PageContainer
              content={content}
              tabList={[
                {
                  tab: '基本信息',
                  key: 'base',
                },
                {
                  tab: '详细信息',
                  key: 'info',
                },
              ]}
              extraContent={
                <Space size={24}>
                  <Statistic title="Feedback" value={1128} prefix={<LikeOutlined />} />
                  <Statistic title="Unmerged" value={93} suffix="/ 100" />
                </Space>
              }
              extra={[
                <Button key="3">操作</Button>,
                <Button key="2">操作</Button>,
                <Button key="1" type="primary">
                  主操作
                </Button>,
              ]}
              footer={[
                <Button key="3">重置</Button>,
                <Button key="2" type="primary">
                  提交
                </Button>,
              ]}
          >
            <div
                style={{
                  height: '120vh',
                }}
            >
              <Result
                  status="404"
                  style={{
                    height: '100%',
                    background: '#fff',
                  }}
                  title="Hello World"
                  subTitle="Sorry, you are not authorized to access this page."
                  extra={<Button type="primary">Back Home</Button>}
              />
            </div>
          </PageContainer>
        </ProLayout>
      </div>
  );
};