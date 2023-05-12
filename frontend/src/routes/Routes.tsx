import {createBrowserRouter} from 'react-router-dom';
import ErrorPage from '../pages/ErrorPage';
import VendingMachine from '../pages/VendingMachine';

const router = createBrowserRouter([
    {
        path: '/',
        element: <VendingMachine/>,
        errorElement: <ErrorPage/>,
    }
]);

export {router};