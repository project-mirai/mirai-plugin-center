import React from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import '@ant-design/pro-layout/dist/layout.css';
import App from './scenes/app/App';
import Login from "./scenes/verify/Login";
import {HashRouter, Redirect, Route, Switch} from "react-router-dom";
import PluginList from "./scenes/app/sub/PluginList";
import '@ant-design/pro-form/dist/form.css';
import '@ant-design/pro-table/dist/table.css';
import '@ant-design/pro-layout/dist/layout.css';
import '@ant-design/pro-card/dist/card.css';
import Register from "./scenes/verify/Register";
import VerifyLayout from "./scenes/verify/VerifyLayout";
import CreatePlugin from "./scenes/app/sub/CreatePlugin";
import PluginInfo from "./scenes/app/sub/PluginInfo";
const RouterConfig = ()=> {
    return (
        <div>

            <HashRouter>
                <Redirect path="" to="/app"/>
                <Switch>
                    <Route path="/app">
                        <App>
                            <Route exact path="/app" component={PluginList}/>
                            <Route exact path="/app/create" component={CreatePlugin}/>
                            <Route path="/app/info/:id" component={PluginInfo}/>
                        </App>
                    </Route>
                    <Route path="/verify">
                        <VerifyLayout>
                            <Route exact path="/verify/login">
                                <Login/>
                            </Route>
                            <Route exact path="/verify/register">
                                <Register/>
                            </Route>
                        </VerifyLayout>
                    </Route>
                </Switch>
            </HashRouter>
        </div>
    );
}

ReactDOM.render(
    <RouterConfig/>,
    document.getElementById('root'),
);