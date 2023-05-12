import React, {useContext} from 'react';
import ApplicationContext from '../context/VendingContext';
import Inventory from "../components/Inventory/Inventory";
import ProductMenu from "../components/ProductMenu/ProductMenu";
import PurchaseButton from "../components/Purchase/Purchase";
import Typography from "@mui/material/Typography";
import Loader from "../components/Loader/Loader";


const VendingMachine = () => {
    const {
        isLoading,
        inventory,
        order,
        orderTotal,
        updateOrder,
        purchaseOrder
    } = useContext(ApplicationContext);
    const products = Object.keys(inventory).map((id) => inventory[parseInt(id)])

    const Display = () => (
        <>
            {Object.keys(inventory).length === 0 ? <Typography>Sold Out</Typography> :
                <>
                    <Inventory products={products}/>
                    <ProductMenu products={products} handleChange={updateOrder} order={order}/>
                    <PurchaseButton orderTotal={orderTotal} handleClick={purchaseOrder}/>
                </>
            }
        </>
    )

    return (
        <>
            {isLoading ? <Loader/> : <Display/>}
        </>
    )
};

export default VendingMachine;
