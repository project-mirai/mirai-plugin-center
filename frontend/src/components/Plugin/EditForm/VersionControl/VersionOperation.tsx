import {VersionFilesProps} from "./FileList";

import {Modal, Button, Space, message} from 'antd';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import axios from "axios";
import FileUpload from "./FileUpload";

const { confirm } = Modal;
export default function (props:VersionFilesProps){
    const {refresh} = props
    const {refreshFile} = props
    const doRefresh = ()=>{
        if(refresh) refresh()
    }
    const doRefreshFile = ()=>{
        if(refreshFile) refreshFile()
    }
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
                doRefresh()
            }catch (e) {
                message.error(e.response.data.message)
            }
        },
    });
    return (
        <Space wrap>
            <FileUpload {...props} refreshFile={doRefreshFile} refresh={doRefresh}/>
            <Button onClick={showDeleteConfirm} type="dashed">
                删除版本
            </Button>
        </Space>)
}