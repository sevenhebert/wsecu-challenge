import React, {useEffect, useState} from 'react';
import VendingContext, {defaultValues} from './VendingContext';
import VendApi from "../api/VendApi";
import {InventoryType, ProductType} from "../models/Product";
import {PurchaseRequestType} from "../models/Purchase";
import {SelectChangeEvent} from "@mui/material";

const api = new VendApi();

interface Props {
    children: React.ReactNode;
}

const VendingContextProvider = ({children}: Props) => {
        const [inventory, setInventory] = useState<InventoryType>(defaultValues.inventory);
        const [order, setOrder] = useState<Map<number, ProductType>>(defaultValues.order);
        const [orderTotal, setOrderTotal] = useState<number>(defaultValues.orderTotal);
        const [isLoading, setIsLoading] = useState(defaultValues.isLoading);
        const [requestError, setRequestError] = useState<string | undefined>(defaultValues.requestError);


        /*
        * Converts the ProductType to InventoryType
        * */
        const productsToInventory = (products: ProductType[]) =>
            products.reduce((inv, prod) => ({...inv, [prod.id]: prod}), {});

        /*
        * Calls the VendApi to get the inventory
        * Sets the inventory state
        * */
        const getInventory = async () => {
            try {
                setIsLoading(true);
                const inventoryResponse = await api.requestInventory();
                const inventoryUpdate = productsToInventory(inventoryResponse)
                setInventory(inventoryUpdate);
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
            orderUpdate.set(id, {...inventory[id], quantity: Number(e.target.value)});
            setOrder(orderUpdate);
            setOrderTotal(getOrderTotal(orderUpdate));
        }

        /*
        * Creates a map from inventory to loop-up price by id
        * */
        const getOrderTotal = (order: Map<number, ProductType>) => Number(Array.from(order.values())
            .reduce((acc, cur) => acc + (cur.quantity * cur.price), 0.00).toFixed(2))

        /*
        * Converts order state to a purchase request
        * */
        const orderToPurchase = (order: Map<number, ProductType>) => Array.from(order.values())
            .reduce((acc, {id, quantity}) => {
                acc.products.push({productId: id, quantity})
                return acc
            }, {products: [], amountPaid: orderTotal} as PurchaseRequestType);

        /*
        * Calls the VendApi to purchase the order
        * Updates the inventory state
        * Resets the order state
        * */
        const purchaseOrder = async () => {
            try {
                setIsLoading(true);
                const data = orderToPurchase(order);
                await api.requestPurchase(data);

                const inventoryResponse = await api.requestInventory();
                const inventoryUpdate = productsToInventory(inventoryResponse)
                setInventory(inventoryUpdate);

                setOrder(defaultValues.order);
                setOrderTotal(defaultValues.orderTotal);
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
                    orderTotal,
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
    }
;

export default VendingContextProvider;
