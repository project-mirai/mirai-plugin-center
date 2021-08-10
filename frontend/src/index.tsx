import React from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import '@ant-design/pro-layout/dist/layout.css';
import App from './scenes/app/App';
import Login from "./scenes/verify/Login";
import {HashRouter, Route, Switch} from "react-router-dom";
import PluginList from "./scenes/app/sub/PluginList";
import '@ant-design/pro-form/dist/form.css';
import '@ant-design/pro-table/dist/table.css';
import '@ant-design/pro-layout/dist/layout.css';
import '@ant-design/pro-card/dist/card.css';
const RouterConfig = ()=> {
    return (
        <HashRouter>
            <Switch>
                <App>
                    <Route exact path="/">
                            <PluginList/>
                    </Route>
                    <Route exact component={Login} path="/login"/>
                </App>
            </Switch>
        </HashRouter>
    );
}

ReactDOM.render(
    <RouterConfig/>,
    document.getElementById('root'),
);