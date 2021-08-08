import React from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import '@ant-design/pro-layout/dist/layout.css';
import App from './scenes/app/App';
import Login from "./scenes/verify/Login";
import {HashRouter, Route, Switch} from "react-router-dom";
const RouterConfig = ()=> {
    return (
    <HashRouter>
            <Switch>
                <Route exact component={ App } path="/"/>
                <Route exact component={ Login } path="/login"/>
            </Switch>
    </HashRouter>
)
}

ReactDOM.render(
    <RouterConfig/>,
    document.getElementById('root'),
);