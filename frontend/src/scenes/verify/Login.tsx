import React, {useState} from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import {Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@material-ui/core";
import {useVerficationFormStyle} from "./VerifyLayout";
import {useHistory} from "react-router";
import request from "../../lib/request";

export default function Login(){
    const classes = useVerficationFormStyle();
    const [open, setOpen] = React.useState(false);
    const [message, setMessage] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const history = useHistory()
    const handleDialogOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    const SubmitForm = ()=>{
        const data = {
            email:username,
            password:password
        }
        request.post('/v1/sso/login',data).then((res)=>{
            console.log(res)
            history.push('/app')
        }).catch((err)=>{
            console.log(err.response)
            if(err.response.data.code === 400) {
                setMessage(err.response.data.message)
                handleDialogOpen()
            }
        })
    }
    return(
        <>
            <Typography component="h1" variant="h5">
                Mirai插件中心-登陆
            </Typography>
            <form className={classes.form} noValidate>
                <Dialog
                    open={open}
                    onClose={handleClose}
                    aria-labelledby="alert-dialog-title"
                    aria-describedby="alert-dialog-description"
                    fullWidth={true}
                >
                    <DialogTitle id="alert-dialog-title">
                        {"错误！"}
                    </DialogTitle>
                    <DialogContent>
                        <DialogContentText id="alert-dialog-description">
                            {message}
                        </DialogContentText>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={handleClose} autoFocus>
                            确定
                        </Button>
                    </DialogActions>
                </Dialog>
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    id="email"
                    label="邮箱地址"
                    name="email"
                    autoComplete="email"
                    value={username}
                    onChange={event => setUsername(event.target.value)}
                    autoFocus
                />
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    name="password"
                    label="密码"
                    type="password"
                    id="password"
                    value={password}
                    onChange={event => setPassword(event.target.value)}
                    autoComplete="current-password"
                />
                <Button
                    fullWidth
                    variant="contained"
                    color="primary"
                    className={classes.submit}
                    onClick={SubmitForm}
                >
                    登陆
                </Button>
                <Grid container>
                    <Grid item xs>
                        <Link variant="body2" onClick={()=>history.push("/verify/resetpassword/sendmail")}>
                            忘记密码
                        </Link>
                    </Grid>
                    <Grid item>
                        <Link variant="body2" onClick={()=>history.push("/verify/register")}>
                            {"注册"}
                        </Link>
                    </Grid>
                </Grid>
            </form>
        </>
    )
}
