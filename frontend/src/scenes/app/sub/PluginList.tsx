import {PageContainer} from "@ant-design/pro-layout";
import {Button, Tag} from "antd";
import React from "react";
import ProCard from '@ant-design/pro-card';
import axios from "axios";
import {PluginInfo} from "../../../models/Plugin";
import SearchBar from "../../../components/SearchBar";
import Meta from "antd/es/card/Meta";
import {DownloadOutlined, InfoOutlined} from "@ant-design/icons";
import {useHistory} from "react-router";

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
    const history = useHistory()
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

            <ProCard colSpan={12} layout={"center"}>
                <SearchBar/>
            </ProCard>

            <ProCard gutter={8} title={"第"+(page+1)+"页"} style={{ marginTop: 8 }} >
                {
                    pluginList.map((item,index) => {
                        return(
                            <ProCard
                                key={index}
                                headerBordered
                                title={
                                    <Meta
                                        title={(item as PluginInfo).name}
                                        description={(item as PluginInfo).id}
                                    />
                                }
                                actions={[
                                    <InfoOutlined key="info" onClick={()=>history.push('/app/info/'+(item as PluginInfo).id)}/>,
                                    <DownloadOutlined key="download" onClick={()=>history.push('/app/info/'+(item as PluginInfo).id)}/>
                                ]}
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
                                <h4>{(item as PluginInfo).info}</h4>
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