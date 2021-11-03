import ProForm, {ProFormText} from "@ant-design/pro-form";
import axios from "axios";
import {Button, message} from "antd";
import ProCard from "@ant-design/pro-card";
import React, {useState} from "react";
import {useHistory} from "react-router";
import {PluginInfoFormParams} from "../EditPluginForm";
type LayoutType = Parameters<typeof ProForm>[0]['layout'];

export default function(props:PluginInfoFormParams) {
    const {id} = props.info
    const [formLayout ] = useState<LayoutType>('horizontal');
    const history = useHistory()
    const formItemLayout =
        formLayout === 'horizontal'
            ? {
                labelCol: { span: 4 },
                wrapperCol: { span: 14 },
            }
            : null;
    const setStatus = async () =>{
        try{
            await axios.patch('/v1/admin/setstate',{
                pluginId:props.info.id,
                state:2
            })
            message.success('切换状态成功');
        }catch (err) {
            message.error(err.response.data.message)
        }
    }
    return <ProCard style={{ marginTop: 8 }} loading={props.loading}>
        <ProForm<{
            name: string;
            id: string;
            info: string;
        }>
            {...formItemLayout}
            layout={formLayout}
            request={async () => {
                return props.info
            }}
            onFinish={async (values) => {
                try{
                    await axios.put('/v1/plugins/'+values.id,{
                        name:values.name,
                        info:values.info
                    })
                    message.success('提交成功');
                }catch (err) {
                    message.error(err.response.data.message)
                }
            }}
            submitter={{
                render: (props, doms) => {
                    return [
                        <Button key={"status"} htmlType="button" onClick={() => setStatus()}>
                            设置状态为启用(admin)
                        </Button>,
                        <Button key={"preview"} htmlType="button" onClick={() => history.push('/app/info/'+id)}>
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
}