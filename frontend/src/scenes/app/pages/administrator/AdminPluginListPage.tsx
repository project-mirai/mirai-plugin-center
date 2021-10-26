import {PageContainer} from "@ant-design/pro-layout";
import React from "react";
import ProCard from '@ant-design/pro-card';
import SearchBar from "../../../../components/SearchBar";
import PluginList from "../../../../components/PluginList/PluginList";



export default () => {
    return (
        <PageContainer
            title={"插件列表"}
            waterMarkProps={{
                content: '',
            }}>

            <ProCard colSpan={12} layout={"center"}>
                <SearchBar/>
            </ProCard>

            <PluginList url={"/v1/plugins/"}/>

        </PageContainer>
    )
}