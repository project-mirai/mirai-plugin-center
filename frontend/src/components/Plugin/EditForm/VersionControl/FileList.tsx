import {useEffect, useState} from "react";
import axios from "axios";
import {Space, Table} from "antd";

export interface VersionFilesProps{
    id:string
    version:string
    refresh?:()=>void
}

interface tableView {
    filename:string
}

export default function(props:VersionFilesProps){
    const [fileList, setFileList] = useState(Array<tableView>())
    useEffect( ()=>{
        axios.get("/v1/plugins/"+props.id+"/"+props.version+"/").then(res=>{
            const arr = []
            for(let i = 0; i < res.data.response.length; i++) {
                arr.push({
                    filename:res.data.response[i]
                })
            }
            setFileList(arr)
        })
    },[])
    //TODO 在ENV中增加变量用于控制API
    const API_URL = "http://localhost:8080"
    const downloadFile = (filename:string)=>window.open(API_URL+'/v1/plugins/'+props.id+"/"+props.version+"/"+filename, '_blank')
    const columns = [
        {
            title: '文件名',
            dataIndex: 'filename',
            key: 'filename',
        },
        {
            title: '操作',
            key: 'action',
            render: (text:any, record:any) => (
                <Space size="middle">
                    <a onClick={()=>downloadFile(record.filename)}>下载 {record.filename}</a>
                    <a>删除</a>
                </Space>
            ),
        },
    ];
    return (
        <>
            <h5>共{fileList.length}个附件</h5>
            <Table dataSource={fileList} columns={columns} />
        </>
    )
}