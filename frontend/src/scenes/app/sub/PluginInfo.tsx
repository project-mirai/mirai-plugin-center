import React, {useEffect, useState} from 'react';
import { Result, Button, Tag, Descriptions} from 'antd';
import {PageContainer} from "@ant-design/pro-layout";
import {EditOutlined} from "@ant-design/icons";
import ProCard from "@ant-design/pro-card";
import axios from "axios";
import {PluginInfo} from "../../../models/Plugin";
import {useHistory} from "react-router";

export default (props:any) => {
    const id = props.match.params.id
    const history = useHistory()
    const [success, setSuccess] = useState(true)
    const [loading, setLoading] = useState(true)
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
        <>
            <ProCard
                loading={loading}
                gutter={8}
                actions={[
                    <EditOutlined key="edit" onClick={()=>history.push('/app/edit/'+id)}/>,
                ]}
                colSpan={12}
                bordered={true}
                layout="default"
                direction="column"
            >
                <Descriptions bordered>
                    <Descriptions.Item label="插件名称">{data.name}</Descriptions.Item>
                    <Descriptions.Item label="包名">{data.id}</Descriptions.Item>
                    <Descriptions.Item label="插件状态">
                        <Tag color={((data as PluginInfo).status==='Accepted')?"green":"red"}>
                            {(data as PluginInfo).status}
                        </Tag>
                    </Descriptions.Item>
                    <Descriptions.Item label="作者">
                        {data.owner.nick}
                    </Descriptions.Item>
                    <Descriptions.Item label="作者邮箱" span={2}>
                        {data.owner.email}
                    </Descriptions.Item>
                    <Descriptions.Item label="插件信息">
                        {data.info}
                    </Descriptions.Item>
                </Descriptions>

            </ProCard>
        </>
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
                extra={<Button onClick={()=>history.push('/')}>返回主页</Button>}
            />
        </div>
    )
    return (

        <PageContainer
            title={"插件信息"}
            waterMarkProps={{
                content: '',
            }}>
            {success?form:result}
        </PageContainer>
    );
};