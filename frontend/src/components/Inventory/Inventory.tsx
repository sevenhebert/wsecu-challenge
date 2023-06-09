import * as React from 'react';
import Typography from '@mui/material/Typography';
import {InventoryType, ProductType} from "../../models/Product";

import LocalDrinkIcon from '@mui/icons-material/LocalDrink';
import LunchDiningIcon from '@mui/icons-material/LunchDining';
import CakeIcon from '@mui/icons-material/Cake';
import {StyledContainer, StyledMenuItem} from './StyledInventory';
import {Divider} from "@mui/material";

interface InventoryProps {
    products: ProductType[];
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
                <Divider/>
                <Typography>${product.price}</Typography>
            </StyledMenuItem>
        ))}
    </StyledContainer>
)

export default Inventory;