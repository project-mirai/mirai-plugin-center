import Search from "antd/es/input/Search";
import React from "react";
import {useHistory} from "react-router";

export default () => {
    const history = useHistory()
    return <Search placeholder="跳转至包名" enterButton onSearch={(id) => history.push('/app/info/'+id)}/>
}