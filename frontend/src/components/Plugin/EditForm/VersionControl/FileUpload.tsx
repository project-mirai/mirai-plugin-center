import {useState} from "react";
import {Button, message, Upload} from "antd";
import {UploadOutlined} from "@ant-design/icons";
import {VersionFilesProps} from "./FileList";


export default function(props:VersionFilesProps) {
    const {refresh} = props
    const doRefresh = ()=>{
        if(refresh) refresh()
    }
    const [urlName, setUrlName] = useState("")
    const handleChange = (info:any) => {
        if (info.file.status !== 'uploading') {
            message.info("正在上传中")
        }
        if (info.file.status === 'done') {
            message.success(`${info.file.name} 上传成功`);
            doRefresh()
        } else if (info.file.status === 'error') {
            message.error(`${info.file.name} 上传失败`);
        }
    }
    const beforeUpload = async(file:any) => {
        setUrlName(`/v1/plugins/${props.id}/${props.version}/${file.name}`)
    }
    const propsUpload = {
        onChange: handleChange,
        multiple: false
    };
    return (
        <Upload method={"PUT"} action={()=>urlName} beforeUpload={beforeUpload} {...propsUpload}>
            <Button icon={<UploadOutlined />}>上传文件</Button>
        </Upload>
    );
}