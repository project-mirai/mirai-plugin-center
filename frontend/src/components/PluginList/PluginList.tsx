import React, {Fragment} from "react";
import ProCard from "@ant-design/pro-card";
import {PluginInfo} from "../../models/Plugin";
import {Pagination, Tag} from "antd";
import {useHistory} from "react-router";
import Meta from "antd/es/card/Meta";
import {InfoOutlined} from "@ant-design/icons";
import request from "../../lib/request";

async function getPluginList(api:string,page:number) {
    const res = await request.get(api,{
        params:{
            page: page - 1
        }
    })
    return res.data.response
}

interface PluginListInfo{
    url:string
}

function pageTotal(rowCount:number, pageSize:number) : number{
        if(rowCount % pageSize==0) {
            return rowCount/pageSize
        }
        return Math.round(rowCount/pageSize) + 1
}

export default (props:PluginListInfo) => {
    const [page, setPage] = React.useState(1);
    const [pluginList, setPluginList] = React.useState([]);
    const [count, setCount] = React.useState(0);
    React.useEffect(()=>{
        request.get(props.url+"count").then((res)=>{
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
                <InfoOutlined key="info" onClick={()=>history.push('/app/info/'+pluginInfoItem.id)}/>
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
    const Pager = ()=>(
        <ProCard layout={"center"}>
            <Pagination defaultCurrent={page} pageSize={20} total={count} onChange={setPage} showQuickJumper/>
        </ProCard>
    )
    return (
        <Fragment>
            <ProCard title={"第"+(page)+"页" + "(共"+count+"个插件/共"+pageTotal(count,20)+"页)"} style={{ marginTop: 8 }} gutter={[16, 16]} wrap>
                {
                    pluginList.map((item,index) => {
                        const pluginInfoItem = item as PluginInfo
                        return (
                            pluginCard(index,pluginInfoItem)
                        )
                    })
                }
            </ProCard>
            <Pager/>
        </Fragment>

    )
}