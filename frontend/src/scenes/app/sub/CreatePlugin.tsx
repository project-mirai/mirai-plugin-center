import React, { useState } from 'react';
import {message, Result, Button} from 'antd';
import ProForm, { ProFormText } from '@ant-design/pro-form';
import {PageContainer} from "@ant-design/pro-layout";
import ProCard from "@ant-design/pro-card";
import axios from "axios";

type LayoutType = Parameters<typeof ProForm>[0]['layout'];

const waitTime = (time: number = 100) => {
    return new Promise((resolve) => {
        setTimeout(() => {
            resolve(true);
        }, time);
    });
};

export default () => {
    const [formLayout ] = useState<LayoutType>('horizontal');
    const [success, setSuccess] = useState(false);
    const formItemLayout =
        formLayout === 'horizontal'
            ? {
                labelCol: { span: 4 },
                wrapperCol: { span: 14 },
            }
            : null;

    const form = (
        <ProCard style={{ marginTop: 8 }} >
            <ProForm<{
                name: string;
                id?: string;
                info?: string;
            }>
                {...formItemLayout}
                layout={formLayout}
                onFinish={async (values) => {
                    await waitTime(500);
                    try{
                        const res = await axios.put('/v1/plugins/'+values.id,{
                            name:values.name,
                            info:values.info
                        })
                        console.log(res)
                        message.success('提交成功');
                        setSuccess(true)
                    }catch (err) {
                        console.log(err)
                        message.error(err.response.data.message)
                    }
                }}
                params={{}}
            >
                <div>
                    <ProFormText
                        width="md"
                        name="id"
                        label="包名"
                        tooltip="例如org.example.mirai.test-plugin"
                        placeholder="请输入包名"
                    />
                    <ProFormText
                        width="md"
                        name="name"
                        label="插件名称"
                        tooltip="例如Mirai Not Core"
                        placeholder="请输入插件名称"
                    />
                    <ProFormText
                        width="md"
                        name="info"
                        label="插件信息"
                        tooltip="填写一些描述信息"
                        placeholder="请输入插件信息"
                    />
                </div>
            </ProForm>
        </ProCard>
    )
    const result = (
        <div
            style={{
                height: '40vh',
            }}
        >
            <Result
                style={{
                    height: '100%',
                    background: '#fff',
                }}
                title="提交成功"
                subTitle="您可以选择编辑插件或者继续创建插件"
                extra={
                    <>
                        <Button type="primary">编辑插件</Button>
                        <Button onClick={()=>setSuccess(false)}>返回创建</Button>
                    </>}
            />
        </div>
    )
    return (

        <PageContainer
            title={"新建插件"}
            waterMarkProps={{
                content: '',
            }}>
            {success?result:form}
        </PageContainer>
    );
};