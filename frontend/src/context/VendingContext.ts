import {createContext} from 'react';
import {InventoryType, ProductType} from "../models/Product";
import {SelectChangeEvent} from "@mui/material";


interface VendingContextType {
    inventory: InventoryType;
    order: Map<number, ProductType>;
    orderTotal: number;
    updateOrder: (id: number, e: SelectChangeEvent<number>) => void;
    purchaseOrder: () => Promise<number | void>;
    isLoading: boolean;
    requestError: string | undefined;
    setRequestError: (error: string | undefined) => void;
}

const defaultValues: VendingContextType = {
    inventory: {},
    order: new Map(),
    orderTotal: 0,
    updateOrder: () => {
    },
    purchaseOrder: () => new Promise(resolve => resolve(NaN)),
    isLoading: false,
    requestError: undefined,
    setRequestError: () => {
    },
};

const VendingContext = createContext<VendingContextType>(defaultValues);
VendingContext.displayName = 'VendingContext';

export {VendingContextType, defaultValues};

export default VendingContext;
