import React, {useEffect, useState} from 'react';
import {PageContainer} from "@ant-design/pro-layout";
import axios from "axios";
import PluginNotFound from "../../../../components/Plugin/PluginNotFound";
import EditPluginForm from "../../../../components/Plugin/EditPluginForm";
export default (props:any) => {
    const [key, setKey] = useState(0)
    const id = props.match.params.id
    const [success, setSuccess] = useState(true)
    const [loading, setLoading] = useState(true)
    const [data, setData] = useState({
        id: "net.mamoe.mirai.mirai-not-core",
        info: "SEE PLUGIN NAME!",
        name: "Mirai Not Core",
        owner: {email: "yellow@sabee.com", nick: "yellow"},
        status: "Accepted",
    })
    console.log(id)
    const doRefresh = ()=>{
        setKey(key+1)
        axios.get('/v1/plugins/'+id).then((res)=>{
            setData(res.data.response)
            setLoading(false)
            console.log(data)
        }).catch(()=>{
            setSuccess(false)
        })
    }
    useEffect(doRefresh,[])

    return (
        <PageContainer
            title={"编辑插件"}
            waterMarkProps={{
                content: '',
            }}>
            {success?<EditPluginForm key={key} refresh={doRefresh} loading={loading} info={data} adminView={true}/>:<PluginNotFound/>}
        </PageContainer>
    );
};