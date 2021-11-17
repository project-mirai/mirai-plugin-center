import React from 'react';
import ReactDOM from 'react-dom';
import 'antd/dist/antd.css';
import '@ant-design/pro-layout/dist/layout.css';
import App from './scenes/app/App';
import Login from "./scenes/verify/Login";
import {HashRouter, Redirect, Route, Switch} from "react-router-dom";
import '@ant-design/pro-form/dist/form.css';
import '@ant-design/pro-table/dist/table.css';
import '@ant-design/pro-layout/dist/layout.css';
import '@ant-design/pro-card/dist/card.css';
import Register from "./scenes/verify/Register";
import VerifyLayout from "./scenes/verify/VerifyLayout";
import SendMail from "./scenes/verify/resetPassword/SendMail";
import ManualResetPassword from "./scenes/verify/resetPassword/ManualResetPassword";
import EditPluginPage from "./scenes/app/pages/developer/EditPluginPage";
import PluginInfoPage from "./scenes/app/pages/developer/PluginInfoPage";
import CreatePluginPage from "./scenes/app/pages/developer/CreatePluginPage";
import PluginListPage from "./scenes/app/pages/PluginListPage";
import AdminPluginListPage from "./scenes/app/pages/administrator/AdminPluginListPage";
const RouterConfig = ()=> {
    const mode = process.env.REACT_APP_API_URL
    console.log(mode)
    return (
        <div>
            <HashRouter>
                <Switch>
                    <Redirect exact from="/" to="/app"/>
                    <Route path="/app">
                        <App>
                            <Route exact path="/app" component={PluginListPage}/>
                            <Route path="/app/info/:id" component={PluginInfoPage}/>
                            <Route exact path="/app/create" component={CreatePluginPage}/>
                            <Route exact path="/app/edit/:id" component={EditPluginPage}/>
                            <Route exact path="/app/admin" component={AdminPluginListPage}/>

                        </App>
                    </Route>
                    <Route path="/verify">
                        <VerifyLayout>
                            <Route exact path="/verify/login" component={Login}/>
                            <Route exact path="/verify/register" component={Register}/>
                            <Route exact path="/verify/resetpassword/sendmail" component={SendMail}/>
                            <Route exact path="/verify/resetpassword/manual" component={ManualResetPassword}/>
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