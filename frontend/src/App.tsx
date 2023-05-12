import React from 'react';
import {RouterProvider} from 'react-router-dom';
import {router} from './routes/Routes';
import {Container} from '@mui/material';

const App = () => {
  return (
    <Container sx={{
      textAlign: 'center',
      minHeight: '100vh',
      display: 'flex',
      flexDirection: 'column',
      alignItems: 'center',
      justifyContent: 'center',
      fontSize: 'calc(10px + 2vmin)',
    }}>
      <RouterProvider router={router}/>
    </Container>
  );
};
export default App;
