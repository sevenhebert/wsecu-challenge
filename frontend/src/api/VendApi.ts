import axios, {AxiosResponse} from 'axios';
import {InventoryType, ProductType} from "../models/Product";
import {PurchaseRequestType} from "../models/Purchase";

class VendApi {
    requestInventory = async (): Promise<ProductType[]> =>
        axios({
            method: 'GET',
            url: 'http://localhost:8080/api/vend/inventory',
            withCredentials: false,
            params: {},
        })
            .then(({data}: AxiosResponse<ProductType[]>) => data)
            .catch((err) => Promise.reject(err));


    requestPurchase = async (data: PurchaseRequestType): Promise<number | void> =>
        axios({
            method: 'POST',
            url: 'http://localhost:8080/api/vend/purchase',
            withCredentials: false,
            data
        })
            .then(({data}: AxiosResponse<number>) => data)
            .catch((err) => Promise.reject(err));

}

export default VendApi;
