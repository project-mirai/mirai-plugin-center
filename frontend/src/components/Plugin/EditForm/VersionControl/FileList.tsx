import {useEffect, useState} from "react";
import {message, Space, Table} from "antd";
import {ExclamationCircleOutlined} from "@ant-design/icons";
import confirm from "antd/lib/modal/confirm";
import {AdminView, isAdminView} from "../../../../models/View";
import request from "../../../../lib/request";

export interface VersionFilesProps extends AdminView {
    id:string
    version:string
    refresh?:()=>void
    refreshFile?:()=>void
}

interface tableView {
    filename:string
}


export default function(props:VersionFilesProps){
    const [fileList, setFileList] = useState(Array<tableView>())
    const refreshList = ()=>{
        request.get("/v1/plugins/"+props.id+"/"+props.version+"/").then(res=>{
            const arr = []
            for(let i = 0; i < res.data.response.length; i++) {
                arr.push({
                    filename:res.data.response[i]
                })
            }
            setFileList(arr)
        })
    }
    useEffect( refreshList,[])


    const showDeleteConfirm = (filename:string) => confirm({
        title: '你确认要删除'+filename+'文件吗？',
        icon: <ExclamationCircleOutlined />,
        content: '该操作不可复原。',
        okText: '确认',
        okType: 'danger',
        cancelText: '取消',
        onOk: async ()=>{
            await request.delete("/v1/plugins/"+props.id+"/"+props.version+"/"+filename)
            message.success("删除成功")
            refreshList()
        },
    });

    const downloadFile = (filename:string)=>window.open(process.env.REACT_APP_API_URL+'/v1/plugins/'+props.id+"/"+props.version+"/"+filename, '_blank')
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
                    <a onClick={()=>downloadFile(record.filename)}>下载</a>
                    {props.adminView}
                    {isAdminView(props)&&<a onClick={()=>showDeleteConfirm(record.filename)}>删除</a>}
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