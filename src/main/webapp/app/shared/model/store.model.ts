import { IStoreItemInstance } from 'app/shared/model/store-item-instance.model';

export interface IStore {
    id?: number;
    name?: string;
    storeItemInstances?: IStoreItemInstance[];
    locationId?: number;
}

export class Store implements IStore {
    constructor(public id?: number, public name?: string, public storeItemInstances?: IStoreItemInstance[], public locationId?: number) {}
}
