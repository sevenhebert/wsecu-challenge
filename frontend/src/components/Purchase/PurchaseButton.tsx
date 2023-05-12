import {Button} from "@mui/material";
import React from "react";
import ShoppingCartIcon from '@mui/icons-material/ShoppingCart';

interface PurchaseButtonProps {
    order: Map<number, number>
    handleClick: () => Promise<number | void>;
}

const PurchaseButton = ({order, handleClick}: PurchaseButtonProps) => (
    <Button
        disabled={order.size === 0}
        sx={{width: '20%', padding: '4px'}}
        variant="contained"
        size="medium"
        onClick={() => handleClick()}
    >
        <ShoppingCartIcon/>
    </Button>
);


export default PurchaseButton;
