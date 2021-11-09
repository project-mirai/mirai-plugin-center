import {PluginInfoFormParams} from "../../EditPluginForm";
import {useEffect, useState} from "react";
import axios from "axios";
import {Collapse} from "antd";
import CollapsePanel from "antd/es/collapse/CollapsePanel";
import FileList from "./FileList";
import VersionOperation from "./VersionOperation";
import {isAdminView} from "../../../../models/View";

export default function(props:PluginInfoFormParams){
    const [versionList,setVersionList] = useState(Array<string>())
    const {refresh} = props
    const doRefresh = ()=>{
        if(refresh) refresh()
    }
    useEffect( ()=>{
        axios.get("/v1/plugins/"+props.info.id+"/versionList").then(res=>setVersionList(res.data.response))
    },[])

    return(
        <>
            <p>共{versionList.length}个版本</p>
            <Collapse defaultActiveKey={[]}>
                {versionList.map((item,index)=>{
                    //TODO 完成后进行文件列表局部刷新，而不是整体刷新
                    return <CollapsePanel header={item} key={index}>
                        <FileList refresh={doRefresh} id={props.info.id} version={item} {...props}/>
                        {isAdminView(props)&&<VersionOperation refresh={doRefresh} id={props.info.id} version={item} {...props}/>}
                    </CollapsePanel>
                })}
            </Collapse>
        </>
    );
}