import {PageContainer} from "@ant-design/pro-layout";
import {Button, Tag} from "antd";
import React from "react";
import ProCard from '@ant-design/pro-card';
import axios from "axios";
import {PluginInfo} from "../../../models/Plugin";

async function getPluginList(page:number) {
    const res = await axios.get('/v1/plugins/',{
        params:{
            page: page
        }
    })
    console.log(res.data.response)
    return res.data.response
}

export default () => {
    const [page, setPage] = React.useState(0);
    const [pluginList, setPluginList] = React.useState([]);
    React.useEffect(() => {
        getPluginList(0).then((res)=>{
            setPluginList(res)
        })
    },[]);
    React.useEffect(()=>{
        getPluginList(page).then((res)=>{
            setPluginList(res)
        })
    },[page])
    return (
        <PageContainer
            title={"插件列表"}
            waterMarkProps={{
            content: '',
        }}>
                <ProCard gutter={8} title={"第"+(page+1)+"页"} style={{ marginTop: 8 }} >
                    {
                        pluginList.map((item,index) => {
                            return(
                                <ProCard
                                    key={index}
                                    headerBordered
                                    title={
                                            <h2>
                                                {(item as PluginInfo).name}
                                            </h2>
                                    }
                                    colSpan={12}
                                    bordered={true}
                                    extra={
                                        ((item as PluginInfo).status==='Accepted')?
                                            <Tag color={"green"}>
                                                {(item as PluginInfo).status}
                                            </Tag>
                                            :
                                            <Tag color={"red"}>
                                                {(item as PluginInfo).status}
                                            </Tag>
                                    }
                                    layout="default"
                                    direction="column"
                                >
                                    <h5>info:{(item as PluginInfo).info}</h5>
                                    <h5>package:{(item as PluginInfo).id}</h5>
                                    <h5>author:{(item as PluginInfo).owner.nick}/{(item as PluginInfo).owner.email}</h5>
                                </ProCard>
                            )
                        })
                    }
                </ProCard>
            <ProCard colSpan={24} layout={"center"}>
                {page>0&&<Button onClick={()=>setPage(page-1)}>上一页</Button>}
                <Button onClick={()=>setPage(page+1)}>下一页</Button>
            </ProCard>

        </PageContainer>
    )
}