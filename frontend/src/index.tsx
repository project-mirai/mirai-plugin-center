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
import EditPlugin from "./scenes/app/sub/EditPlugin";
import SendMail from "./scenes/verify/resetPassword/SendMail";
const RouterConfig = ()=> {
    return (
        <div>
            <HashRouter>
                <Switch>
                    <Redirect exact from="/" to="/app"/>
                    <Route path="/app">
                        <App>
                            <Route exact path="/app" component={PluginList}/>
                            <Route path="/app/info/:id" component={PluginInfo}/>
                            <Route exact path="/app/create" component={CreatePlugin}/>
                            <Route exact path="/app/edit/:id" component={EditPlugin}/>
                        </App>
                    </Route>
                    <Route path="/verify">
                        <VerifyLayout>
                            <Route exact path="/verify/login" component={Login}/>
                            <Route exact path="/verify/register" component={Register}/>
                            <Route exact path="/verify/resetpassword/sendmail" component={SendMail}/>
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