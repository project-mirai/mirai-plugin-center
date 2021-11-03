import {Button, Form, Input, message, Upload} from "antd";
import ProCard from "@ant-design/pro-card";
import React, {useState} from "react";
import {PluginInfoFormParams} from "../EditPluginForm";
import {UploadOutlined} from "@ant-design/icons";
import axios from "axios";
import VersionList from "./VersionControl/VersionList";

export default function(props:PluginInfoFormParams) {
    const {id} = props.info
    const [hasPluginVersion, setHasPluginVersion] = useState(false)
    const [form] = Form.useForm();
    const [version, setVersion] = useState("")
    const onFinish = async (values: any) => {
        try{
            await axios.put("/v1/plugins/"+id+"/"+values.version)
            setVersion(values.version)
            setHasPluginVersion(true)
        }catch (e) {
            message.error(e.response.data.message)
        }
    };

    return <ProCard style={{ marginTop: 8 }} loading={props.loading}>
        <VersionList {...props}/>
        <Form form={form} onFinish={onFinish}>
            <Form.Item
                label="版本号"
                name="version"
                rules={[{ required: true, message: '请输入版本号!' }]}>
                <Input disabled={hasPluginVersion} placeholder="输入版本号" />
            </Form.Item>
            {hasPluginVersion?
                <Upload
                action={"/v1/plugins/"+id+"/"+version+"/test.zip"}
                method={"put"}
            >
                <Button icon={<UploadOutlined />}>点击上传</Button>
            </Upload>:<Button  htmlType="submit">确定</Button>}
        </Form>
    </ProCard>
}