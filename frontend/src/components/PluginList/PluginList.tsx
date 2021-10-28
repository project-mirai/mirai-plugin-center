import axios from "axios";
import React, {Fragment} from "react";
import ProCard from "@ant-design/pro-card";
import {PluginInfo} from "../../models/Plugin";
import {Button, Tag} from "antd";
import {useHistory} from "react-router";
import Meta from "antd/es/card/Meta";
import {DownloadOutlined, InfoOutlined} from "@ant-design/icons";

async function getPluginList(api:string,page:number) {
    const res = await axios.get(api,{
        params:{
            page: page
        }
    })
    return res.data.response
}

interface PluginListInfo{
    url:string
}

export default (props:PluginListInfo) => {
    const [page, setPage] = React.useState(0);
    const [pluginList, setPluginList] = React.useState([]);
    const [count, setCount] = React.useState(0);
    React.useEffect(()=>{
        axios.get(props.url+"count").then((res)=>{
            setCount(res.data.response.countL)
        })
    },[])
    React.useEffect(()=>{
        getPluginList(props.url,page).then((res)=>{
            setPluginList(res)
        })
    },[page])
    const history = useHistory()
    const pluginCard = (index:number, pluginInfoItem:PluginInfo) => {
        return <ProCard
            key={index}
            title={
                <Meta
                    title={pluginInfoItem.name}
                    description={pluginInfoItem.id}
                />
            }
            actions={[
                <InfoOutlined key="info" onClick={()=>history.push('/app/info/'+pluginInfoItem.id)}/>,
                <DownloadOutlined key="download" onClick={()=>history.push('/app/info/'+pluginInfoItem.id)}/>
            ]}
            colSpan={12}
            bordered={true}
            extra={
                <Tag color={pluginInfoItem.status==='Accepted'?"green":"red"}>
                    {pluginInfoItem.status}
                </Tag>
            }
            layout="default"
            direction="column"
        >
            <h4>{pluginInfoItem.info}</h4>
        </ProCard>

    }
    return (
        <Fragment>
            <ProCard title={"第"+(page+1)+"页" + "(共"+count+"个插件/"+Math.round(count/20)+"页)"} style={{ marginTop: 8 }} gutter={[16, 16]} wrap>
                {
                    pluginList.map((item,index) => {
                        const pluginInfoItem = item as PluginInfo
                        return (
                            pluginCard(index,pluginInfoItem)
                        )
                    })
                }
            </ProCard>

            <ProCard layout={"center"}>
                <Button disabled={page<1} onClick={()=>setPage(page-1)}>上一页</Button>
                <Button disabled={page+1 > Math.round(count/20)} onClick={()=>setPage(page+1)}>下一页</Button>
            </ProCard>
        </Fragment>

    )
}