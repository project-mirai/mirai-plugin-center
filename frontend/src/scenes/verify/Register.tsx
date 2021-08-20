import React, {useState} from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import axios from "axios";
import {Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@material-ui/core";
import {useVerficationFormStyle} from "./VerifyLayout";
import Typography from "@material-ui/core/Typography";
import {useHistory} from "react-router";

export default function Register(){
    const classes = useVerficationFormStyle();
    const [open, setOpen] = React.useState(false);
    const [nick, setNick] = useState('');
    const [message, setMessage] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const history = useHistory()
    const handleDialogOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
    };
    const SubmitForm = ()=> {
        const data = {
            email:username,
            password:password,
            nick:nick
        }
        axios.post('/v1/sso/register',data).then((res)=>{
            if(res.data.code === 200) {
                setMessage("注册成功")
                handleDialogOpen()
                history.push('/app')
            }
        }).catch((err)=>{
            setMessage(err.response.data.message)
            handleDialogOpen()
        })
    }
    return (
        <>
            <Typography component="h1" variant="h5">
                Mirai插件中心-注册
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
                        {"提示！"}
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
                    label="昵称"
                    name="nick"
                    autoComplete="nick"
                    value={nick}
                    onChange={event => setNick(event.target.value)}
                    autoFocus
                />
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
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
                    value={password}
                    onChange={event => setPassword(event.target.value)}
                    autoComplete="current-password"
                />
                <TextField
                    variant="outlined"
                    margin="normal"
                    required
                    fullWidth
                    name="confirm"
                    label="确认密码"
                    type="password"
                    value={confirmPassword}
                    onChange={event => setConfirmPassword(event.target.value)}
                    autoComplete="current-password"
                />
                <Button
                    fullWidth
                    variant="contained"
                    color="primary"
                    className={classes.submit}
                    onClick={SubmitForm}
                >
                    注册
                </Button>
                <Grid container>
                    <Grid item xs>
                    </Grid>
                    <Grid item>
                        <Link href="#" variant="body2">
                            {"返回登陆"}
                        </Link>
                    </Grid>
                </Grid>
            </form>
        </>
    );

}
