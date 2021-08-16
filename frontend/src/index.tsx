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
import Register from "./scenes/verify/Register";
import VerifyLayout from "./scenes/verify/VerifyLayout";
import CreatePlugin from "./scenes/app/sub/CreatePlugin";
const RouterConfig = ()=> {
    return (
        <div>

            <HashRouter>
                <Switch>
                    <Route path="/app">
                        <App>
                            <Route exact path="/app">
                                <PluginList/>
                            </Route>
                            <Route exact path="/app/create">
                                <CreatePlugin/>
                            </Route>
                            <Route exact path="/app/create">
                                <CreatePlugin/>
                            </Route>
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