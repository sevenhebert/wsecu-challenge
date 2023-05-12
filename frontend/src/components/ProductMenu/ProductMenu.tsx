import {InputLabel, MenuItem, Select, SelectChangeEvent} from "@mui/material";
import React from "react";
import {ProductType} from "../../models/Product";
import {StyledDiv, StyledMenu} from "./StyledMenu";

interface ProductMenuProps {
    products: ProductType[];
    handleChange: (id: number, e: SelectChangeEvent<number>) => void;
    order: Map<number, ProductType>;
}

const ProductMenu = ({products, handleChange, order}: ProductMenuProps) => (
    <StyledDiv>
        {products.map(({id, name, quantity}, idx) => (
            <StyledMenu key={idx}>
                <InputLabel>{name}</InputLabel>
                <Select defaultValue={0} value={(order.get(id)?.quantity)} onChange={e => handleChange(id, e)}>
                    {Array.from({length: quantity + 1}, (_, i) => <MenuItem key={i} value={i}>{i}</MenuItem>)}
                </Select>
            </StyledMenu>
        ))}
    </StyledDiv>
)
export default ProductMenu;
