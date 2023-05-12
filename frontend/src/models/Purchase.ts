interface ProductOrderType {
  productId: number;
  quantity: number;
}

interface PurchaseRequestType {
  products: ProductOrderType[];
  amountPaid: number;
}

export {ProductOrderType, PurchaseRequestType};
