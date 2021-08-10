import React, {useState} from 'react';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import axios from "axios";
import {Dialog, DialogActions, DialogContent, DialogContentText, DialogTitle} from "@material-ui/core";


function Copyright() {
    return (
        <Typography variant="body2" color="textSecondary" align="center">
            {'Copyright © '}
            <Link color="inherit" href="https://github.com/project-mirai/mirai-plugin-center">
                Mirai
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

const useStyles = makeStyles((theme) => ({
    paper: {
        marginTop: theme.spacing(8),
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center'
    },
    avatar: {
        margin: theme.spacing(1),
        backgroundColor: theme.palette.secondary.main,
    },
    form: {
        width: '100%', // Fix IE 11 issue.
        marginTop: theme.spacing(1),
    },
    submit: {
        margin: theme.spacing(3, 0, 2),
    },
}));

function PageLoginForm(){
    const classes = useStyles();
    const [open, setOpen] = React.useState(false);
    const [message, setMessage] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

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
        axios.post('/v1/sso/login',data).then((res)=>{
            console.log(res)
        }).catch((err)=>{
            console.log(err.response)
            if(err.response.data.code === 400) {
                setMessage(err.response.data.message)
                handleDialogOpen()
            }
        })
    }
    return(
        <form className={classes.form} noValidate>
            <Dialog
                open={open}
                onClose={handleClose}
                aria-labelledby="alert-dialog-title"
                aria-describedby="alert-dialog-description"
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
                    <Link href="#" variant="body2">
                        忘记密码
                    </Link>
                </Grid>
                <Grid item>
                    <Link href="#" variant="body2">
                        {"注册"}
                    </Link>
                </Grid>
            </Grid>
        </form>
    )
}

export default function Login(){
    const classes = useStyles();
    return (
        <Container component="main" maxWidth="xs">
            <CssBaseline />
            <div className={classes.paper}>
                <Typography component="h1" variant="h5">
                    Mirai插件中心-登陆
                </Typography>
                <PageLoginForm/>
            </div>
            <Box mt={8}>
                <Copyright />
            </Box>
        </Container>
    );
}
