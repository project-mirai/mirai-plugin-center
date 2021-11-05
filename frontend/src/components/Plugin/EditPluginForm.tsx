import React from "react";
import {PluginInfo} from "../../models/Plugin";
import EditPluginInfoForm from "./EditForm/EditPluginInfoForm";
import PluginVersionControlForm from "./EditForm/PluginVersionControlForm";

export interface PluginInfoFormParams {
    loading:boolean
    info:PluginInfo
    refresh ?: ()=>void
}

export default function(props:PluginInfoFormParams) {
    const {refresh} = props
    const doRefresh = ()=>{
        if(refresh) refresh()
    }
    return <>
        <EditPluginInfoForm refresh={doRefresh} {...props}/>
        <PluginVersionControlForm refresh={doRefresh} {...props}/>
    </>
}