import {PluginInfoFormParams} from "../../EditPluginForm";
import {useEffect, useState} from "react";
import axios from "axios";
import {Collapse} from "antd";
import CollapsePanel from "antd/es/collapse/CollapsePanel";
import FileList from "./FileList";
import VersionOperation from "./VersionOperation";

export default function(props:PluginInfoFormParams){
    const [versionList,setVersionList] = useState(Array<string>())
    const {refresh} = props
    const doRefresh = ()=>{
        if(refresh) refresh()
    }
    useEffect( ()=>{
        axios.get("/v1/plugins/"+props.info.id+"/versionList").then(res=>setVersionList(res.data.response))
    },[])

    function callback(key:any) {
        console.log(key);
    }

    return(
        <Collapse defaultActiveKey={[]} onChange={callback}>
            {versionList.map((item,index)=>{
                return <CollapsePanel header={item} key={index}>
                    <FileList refresh={doRefresh} id={props.info.id} version={item}/>
                    <VersionOperation refresh={doRefresh} id={props.info.id} version={item}/>
                </CollapsePanel>
            })}
        </Collapse>
    );
}