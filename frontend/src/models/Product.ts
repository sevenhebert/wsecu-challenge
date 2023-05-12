interface ProductType {
    id: number,
    name: string,
    price: number
    quantity: number
}

interface InventoryType {
    [id: number]: ProductType
}

export {ProductType, InventoryType};