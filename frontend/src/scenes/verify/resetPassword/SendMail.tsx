import React, {useState} from 'react';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Typography from '@material-ui/core/Typography';
import {Container, Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@material-ui/core";
import {useVerficationFormStyle} from "../VerifyLayout";
import {useHistory} from "react-router";
import request from "../../../lib/request";

export default function SendMail(){
    const classes = useVerficationFormStyle();
    const [open, setOpen] = React.useState(false);
    const [message, setMessage] = useState('');
    const [email, setEmail] = useState('');
    const [success, setSuccess] = useState(false);
    const history = useHistory()
    const handleDialogOpen = () => {
        setOpen(true);
    };

    const handleClose = () => {
        setOpen(false);
        history.push('/verify/resetpassword/sendmail')
    };
    const SubmitForm = ()=>{
        request.get('/v1/sso/resetPassword?email='+email).then((res)=>{
            console.log(res)
            setMessage("发送成功")
            setSuccess(true)
        }).catch((err)=>{
            console.log(err.response)
            setMessage(err.response.data.message)
            handleDialogOpen()
        })
    }
    const dialog = (
        <Dialog
            open={open}
            onClose={handleClose}
            aria-labelledby="alert-dialog-title"
            aria-describedby="alert-dialog-description"
            fullWidth={true}
        >
            <DialogTitle id="alert-dialog-title">
                {"提示"}
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
    )
    const form = (
        <>
            <Typography component="h1" variant="h5">
                Mirai插件中心-重置密码
            </Typography>
            <form className={classes.form} noValidate>
                {dialog}
                <TextField
                    margin="normal"
                    required
                    fullWidth
                    id="email"
                    label="邮箱地址"
                    name="email"
                    autoComplete="email"
                    value={email}
                    onChange={event => setEmail(event.target.value)}
                    autoFocus
                />
                <Button
                    fullWidth
                    variant="contained"
                    color="primary"
                    className={classes.submit}
                    onClick={SubmitForm}
                >
                    发送验证邮件
                </Button>
                <Grid container>
                    <Grid item xs>
                    </Grid>
                    <Grid item>
                        <Link variant="body2" onClick={()=>history.push("/verify/login")}>
                            {"返回登陆"}
                        </Link>
                    </Grid>
                </Grid>
            </form>
        </>
    )
    const successPage = (
        <Container component="main">
            <Typography variant="h2" component="h1" gutterBottom>
                发送成功
            </Typography>
            <Typography variant="h5">
                请到您的邮件箱中查看验证邮件
                <br/>
                {'<'+email+'>'}
            </Typography>
        </Container>
    )
    return(
        success ? successPage : form
    )
}
