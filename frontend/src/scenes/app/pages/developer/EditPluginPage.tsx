import React, {useEffect, useState} from 'react';
import {Result, Button, message} from 'antd';
import {PageContainer} from "@ant-design/pro-layout";
import ProCard from "@ant-design/pro-card";
import axios from "axios";
import ProForm, {ProFormText} from "@ant-design/pro-form";
import {useHistory} from "react-router";
type LayoutType = Parameters<typeof ProForm>[0]['layout'];

export default (props:any) => {
    const [formLayout ] = useState<LayoutType>('horizontal');
    const formItemLayout =
        formLayout === 'horizontal'
            ? {
                labelCol: { span: 4 },
                wrapperCol: { span: 14 },
            }
            : null;
    const id = props.match.params.id
    const [success, setSuccess] = useState(true)
    const [loading, setLoading] = useState(true)
    const history = useHistory()
    const [data, setData] = useState({
        id: "net.mamoe.mirai.mirai-not-core",
        info: "SEE PLUGIN NAME!",
        name: "Mirai Not Core",
        owner: {email: "yellow@sabee.com", nick: "yellow"},
        status: "Accepted",
    })
    console.log(id)
    useEffect(()=>{
        axios.get('/v1/plugins/'+id).then((res)=>{
            setData(res.data.response)
            setLoading(false)
            console.log(data)
        }).catch(()=>{
            setSuccess(false)
        })
    },[])

    const form = (
        <ProCard style={{ marginTop: 8 }} loading={loading} >
            <ProForm<{
                name: string;
                id: string;
                info: string;
            }>
                {...formItemLayout}
                layout={formLayout}
                request={async () => {
                    const res = await axios.get('/v1/plugins/'+id)

                    return {
                        name : res.data.response.name,
                        id:res.data.response.id,
                        info:res.data.response.info
                    }
                }}
                onFinish={async (values) => {
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
                submitter={{
                    render: (props, doms) => {
                        return [
                            <Button htmlType="button" onClick={() => history.push('/app/info/'+id)}>
                                设置状态为启用(admin)
                            </Button>,
                            <Button htmlType="button" onClick={() => history.push('/app/info/'+id)}>
                                查看该插件信息
                            </Button>,
                            ...doms,
                        ];
                    },
                }}
            >
                <div>
                    <ProFormText
                        width="md"
                        disabled={true}
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
    );
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
                status="warning"
                title="错误"
                subTitle="您查找的ID并不存在"
                extra={<Button onClick={()=>setSuccess(false)}>返回主页</Button>}
            />
        </div>
    )
    return (

        <PageContainer
            title={"编辑插件"}
            waterMarkProps={{
                content: '',
            }}>
            {success?form:result}
        </PageContainer>
    );
};