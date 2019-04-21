export interface IStoreItemInstance {
    id?: number;
    price?: number;
    storeItemId?: number;
    storeId?: number;
}

export class StoreItemInstance implements IStoreItemInstance {
    constructor(public id?: number, public price?: number, public storeItemId?: number, public storeId?: number) {}
}
