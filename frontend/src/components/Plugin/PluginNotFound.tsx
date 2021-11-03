import {Button, Result} from "antd";
import React from "react";
import {useHistory} from "react-router";

export default function (){
    const history = useHistory()
    return (

        <div
            style={{
                height: '40vh',
            }}
        >
            <Result
                style={{
                    height: '100%',
                    background: '#fff',
                }}
                status="warning"
                title="错误"
                subTitle="您查找的ID并不存在"
                extra={<Button onClick={()=>history.push("")}>返回主页</Button>}
            />
        </div>
    )
}