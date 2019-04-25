export interface IStoreItemInstance {
    id?: number;
    price?: number;
    storeItemName?: string;
    storeItemId?: number;
    storeName?: string;
    storeId?: number;
}

export class StoreItemInstance implements IStoreItemInstance {
    constructor(
        public id?: number,
        public price?: number,
        public storeItemName?: string,
        public storeItemId?: number,
        public storeName?: string,
        public storeId?: number
    ) {}
}
