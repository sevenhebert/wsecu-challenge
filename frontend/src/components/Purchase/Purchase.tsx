import {Button} from '@mui/material';
import React from 'react';
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';
import {StyledContainer} from './StyledPurchase';

interface PurchaseProps {
    orderTotal: number;
    handleClick: () => Promise<number | void>;
}

const Purchase = ({orderTotal, handleClick}: PurchaseProps) => (
    <>
        <StyledContainer>
            Total ${orderTotal}
        </StyledContainer>
        <Button
            disabled={orderTotal === 0.00}
            sx={{width: '20%', padding: '4px'}}
            variant='contained'
            size='medium'
            onClick={() => handleClick()}
        >
            <ShoppingCartIcon/>
        </Button>
    </>
)


export default Purchase;
