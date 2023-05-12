import React, {useEffect, useState} from 'react';
import VendingContext, {defaultValues} from './VendingContext';
import VendApi from "../api/VendApi";
import {InventoryType} from "../models/Product";
import {PurchaseRequestType} from "../models/Purchase";
import {SelectChangeEvent} from "@mui/material";

const api = new VendApi();

interface Props {
    children: React.ReactNode;
}

const VendingContextProvider = ({children}: Props) => {
    const [inventory, setInventory] = useState<InventoryType[]>(defaultValues.inventory);
    const [order, setOrder] = useState<Map<number, number>>(defaultValues.order);
    const [isLoading, setIsLoading] = useState(defaultValues.isLoading);
    const [requestError, setRequestError] = useState<string | undefined>(defaultValues.requestError);

    /*
    * Calls the VendApi to get the inventory
    * Sets the inventory state
    * */
    const getInventory = async () => {
        try {
            setIsLoading(true);
            const inventoryResponse = await api.requestInventory();
            setInventory(inventoryResponse);
        } catch (err) {
            console.error(err);
            setInventory([]);
            setRequestError('Could not fetch inventory');
        } finally {
            setIsLoading(false);
        }
    };

    /*
    * Initial call to get the inventory
    * */
    useEffect(() => {
        getInventory();
    }, []);

    /*
    * Handles updating the order state
    * */
    const updateOrder = (id: number, e: SelectChangeEvent<number>) => {
        e.preventDefault();
        const orderUpdate = new Map(order);
        orderUpdate.set(id, Number(e.target.value));
        setOrder(orderUpdate);
    }

    /*
    * Creates a map from inventory to loop-up price by id
    * */
    const priceMap = (i: InventoryType[]) => i.reduce((acc, cur) => acc.set(cur.id, cur.price), new Map<number, number>())

    /*
    * Converts order state to a purchase request
    * */
    const orderToPurchase = (order: Map<number, number>) => {
        const prices = priceMap(inventory)
        return Array.from(order.entries())
            .reduce((acc, [productId, quantity]) => {
                acc.products.push({productId, quantity});
                acc.amountPaid += (prices.get(productId) || 0) * quantity;
                return acc;
            }, {products: [], amountPaid: 0.00} as PurchaseRequestType);
    }

    /*
    * Calls the VendApi to purchase the order
    * Updates the inventory state
    * Resets the order state
    * */
    const purchaseOrder = async () => {
        try {
            setIsLoading(true);
            const data = orderToPurchase(order);
            console.log("data", data)
            await api.requestPurchase(data);

            const remainingInventory = await api.requestInventory();
            setInventory(remainingInventory);

            setOrder(defaultValues.order);
        } catch (err) {
            console.error(err);
            setRequestError('Error purchasing order');
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <VendingContext.Provider
            value={{
                inventory,
                order,
                updateOrder,
                purchaseOrder,
                isLoading,
                requestError,
                setRequestError,
            }}
        >
            {children}
        </VendingContext.Provider>
    );
};

export default VendingContextProvider;
