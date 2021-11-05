import {VersionFilesProps} from "./FileList";

import {Modal, Button, Space, message} from 'antd';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import axios from "axios";

const { confirm } = Modal;

function showConfirm() {
    confirm({
        title: 'Do you Want to delete these items?',
        icon: <ExclamationCircleOutlined />,
        content: 'Some descriptions',
        onOk() {
            console.log('OK');
        },
        onCancel() {
            console.log('Cancel');
        },
    });
}

export default function (props:VersionFilesProps){
    const showDeleteConfirm = () => confirm({
        title: '你确认要删除'+props.version+'版本吗？',
        icon: <ExclamationCircleOutlined />,
        content: '删除该版本同时会将该版本下所有文件删除。',
        okText: '确认',
        okType: 'danger',
        cancelText: '取消',
        onOk: async ()=>{
            try{
                await axios.delete("/v1/plugins/"+props.id+"/"+props.version)
                message.success("删除成功")
                if(props.refresh) {
                    props.refresh()
                }
            }catch (e) {
                message.error(e.response.data.message)
            }
        },
        onCancel() {
            console.log('Cancel');
        },
    });
    return (
        <Space wrap>
            <Button onClick={showConfirm}>新增版本文件</Button>
            <Button onClick={showDeleteConfirm} type="dashed">
                删除版本
            </Button>
        </Space>)
}