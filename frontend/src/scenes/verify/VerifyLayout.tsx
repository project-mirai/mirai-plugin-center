import React from 'react';
import CssBaseline from '@material-ui/core/CssBaseline';
import Link from '@material-ui/core/Link';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';

export const useVerficationFormStyle = makeStyles((theme) => ({
    paper: {
        //marginTop: theme.spacing(8),
        height: '100%',
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

export default function VerifyLayout(props:any){
    const classes = useVerficationFormStyle();
    return (
        <Container component="main" maxWidth="xs">
            <Grid
                container
                spacing={0}
                direction="column"
                alignItems="center"
                style={{ minHeight: '100vh' }}
            >

                <Grid
                    className={classes.paper}
                    item xs={12}>
                    {props.children}
                </Grid>
                <CssBaseline />
                <Box mt={8}>
                    <Copyright />
                </Box>

            </Grid>
        </Container>
    );
}
