import ProCard from "@ant-design/pro-card";
import React from "react";
import {PluginInfoFormParams} from "../EditPluginForm";
import VersionList from "./VersionControl/VersionList";
import VersionCreate from "./VersionControl/VersionCreate";

export default function(props:PluginInfoFormParams) {
    const {refresh} = props
    const doRefresh = ()=>{
        if(refresh) refresh()
    }
    return <ProCard style={{ marginTop: 8 }} loading={props.loading}>
        <VersionList refresh={doRefresh} {...props}/>
        <VersionCreate refresh={doRefresh} {...props}/>
    </ProCard>
}