import React, { useState } from 'react';
import {Modal, Button, Form, Input, message} from 'antd';
import {PluginInfoFormParams} from "../../EditPluginForm";
import axios from "axios";
export default function(props:PluginInfoFormParams) {
    const [form] = Form.useForm();
    const [isModalVisible, setIsModalVisible] = useState(false);

    const showModal = () => {
        setIsModalVisible(true);
    };

    const handleOk = () => {
        setIsModalVisible(false);
    };

    const handleCancel = () => {
        setIsModalVisible(false);
    };

    const onFinish = async (values: any) => {
        try{
            await axios.put("/v1/plugins/"+props.info.id+"/"+values.version)
            setIsModalVisible(false)
            if(props.refresh) {
                props.refresh()
            }
        }catch (e) {
            message.error(e.response.data.message)
        }
    };

    return (
        <>
            <Button type="primary" onClick={showModal}>
                添加新版本
            </Button>
            <Modal title="请输入版本号" visible={isModalVisible} onOk={handleOk} onCancel={handleCancel} footer={null}>
                <Form form={form} onFinish={onFinish}>
                    <Form.Item
                        label="版本号"
                        name="version"
                        rules={[{ required: true, message: '请输入版本号!' }]}>
                        <Input placeholder="输入版本号" />
                    </Form.Item>
                    <Button  htmlType="submit">确定</Button>
                </Form>
            </Modal>
        </>
    );
};