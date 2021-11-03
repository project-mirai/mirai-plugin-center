import React from "react";
import {BasicPluginInfo} from "../../models/Plugin";
import EditPluginInfoForm from "./EditForm/EditPluginInfoForm";
import PluginVersionControlForm from "./EditForm/PluginVersionControlForm";

export interface PluginInfoFormParams {
    loading:boolean
    info:BasicPluginInfo
}

export default function(props:PluginInfoFormParams) {
    return <>
        <EditPluginInfoForm {...props}/>
        <PluginVersionControlForm {...props}/>
    </>
}