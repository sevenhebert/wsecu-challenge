import {createContext} from 'react';
import {InventoryType} from "../models/Product";
import {SelectChangeEvent} from "@mui/material";

interface VendingContextType {
    inventory: InventoryType[];
    order: Map<number, number>;
    updateOrder: (id: number, e: SelectChangeEvent<number>) => void;
    purchaseOrder: () => Promise<number | void>;
    isLoading: boolean;
    requestError: string | undefined;
    setRequestError: (error: string | undefined) => void;
}

const defaultValues: VendingContextType = {
    inventory: [],
    order: new Map<number, number>(),
    updateOrder: () => {},
    purchaseOrder: () => new Promise(resolve => resolve(NaN)),
    isLoading: false,
    requestError: undefined,
    setRequestError: () => {},
};

const VendingContext = createContext<VendingContextType>(defaultValues);
VendingContext.displayName = 'VendingContext';

export {VendingContextType, defaultValues};

export default VendingContext;
