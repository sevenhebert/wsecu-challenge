import React, {useContext} from 'react';
import ApplicationContext from '../context/VendingContext';
import Inventory from "../components/Inventory/Inventory";
import ProductMenu from "../components/ProductMenu/ProductMenu";
import PurchaseButton from "../components/Purchase/PurchaseButton";
import Loader from "../components/Loader/Loader";


const VendingMachine = () => {
    const {
        isLoading,
        inventory,
        order,
        updateOrder,
        purchaseOrder
    } = useContext(ApplicationContext);

    return <>{isLoading ? (<Loader/>) :
        <>
            <Inventory products={inventory}/>
            <ProductMenu products={inventory} handleChange={updateOrder} order={order}/>
            <PurchaseButton order={order} handleClick={purchaseOrder}/>
        </>
    }</>
};

export default VendingMachine;
