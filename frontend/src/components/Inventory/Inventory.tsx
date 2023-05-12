import * as React from 'react';
import Typography from '@mui/material/Typography';
import {InventoryType} from "../../models/Product";

import LocalDrinkIcon from '@mui/icons-material/LocalDrink';
import LunchDiningIcon from '@mui/icons-material/LunchDining';
import CakeIcon from '@mui/icons-material/Cake';
import {StyledContainer, StyledMenuItem} from './StyledInventory';

interface InventoryProps {
    products: InventoryType[];
}

const Display = ({productName}: { productName: string }) => {
    if (productName === "Coke") return (<LocalDrinkIcon/>)
    else if (productName === "Doritos") return (<LunchDiningIcon/>)
    else return (<CakeIcon/>)
}

const Inventory = ({products}: InventoryProps) => (
    <StyledContainer>
        {products.map((product, idx) => (
            <StyledMenuItem key={idx}>
                <Typography><Display productName={product.name}/></Typography>
                <Typography>{product.name}</Typography>
                <Typography>{product.quantity} Remaining</Typography>
            </StyledMenuItem>
        ))}
    </StyledContainer>
)

export default Inventory;