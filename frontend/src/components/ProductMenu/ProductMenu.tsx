import {InputLabel, MenuItem, Select, SelectChangeEvent} from "@mui/material";
import React from "react";
import {InventoryType} from "../../models/Product";
import {StyledMenu} from "./StyledMenu";

interface ProductMenuProps {
    products: InventoryType[];
    handleChange: (id: number, e: SelectChangeEvent<number>) => void;
    order: Map<number, number>;
}

const ProductMenu = ({products, handleChange, order}: ProductMenuProps) => (
    <>
        {products.map(({id, name, quantity}, idx) => (
            <StyledMenu key={idx}>
                <InputLabel>{name}</InputLabel>
                <Select value={(order.get(id) || 0)} onChange={e => handleChange(id, e)}>
                    {Array.from({length: quantity}, (_, i) => <MenuItem key={i} value={i}>{i}</MenuItem>)}
                </Select>
            </StyledMenu>
        ))}
    </>
)
export default ProductMenu;
