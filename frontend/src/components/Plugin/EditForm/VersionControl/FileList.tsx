import {useEffect, useState} from "react";
import axios from "axios";

export interface VersionFilesProps{
    id:string
    version:string
    refresh?:()=>void
}
export default function(props:VersionFilesProps){
    const [fileList, setFileList] = useState(Array<string>())
    useEffect( ()=>{
        axios.get("/v1/plugins/"+props.id+"/"+props.version+"/").then(res=>setFileList(res.data.response))
    },[])
    return (
        <>
            <h5>共{fileList.length}个附件</h5>
            {fileList.map((item,index)=><h5 key={index}>{item}</h5>)}
        </>
    )
}