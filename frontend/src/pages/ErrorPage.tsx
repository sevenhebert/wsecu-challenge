import {useNavigate, useRouteError} from 'react-router-dom';
import {Button} from '@mui/material';
import React from 'react';

const ErrorPage = () => {
  let error = useRouteError();
  const navigate = useNavigate();
  console.error(error);

  return (
    <div id="error-page">
      <h1>Oops!</h1>
      <p>Sorry, an unexpected error has occurred</p>
      <Button
        sx={{height: '40px'}}
        variant="contained"
        color="success"
        onClick={() => navigate('/')}
      >
        Return to home
      </Button>
    </div>

  );
};

export default ErrorPage;
