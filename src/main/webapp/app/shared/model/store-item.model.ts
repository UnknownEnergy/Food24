import { IGroceryList } from 'app/shared/model/grocery-list.model';

export interface IStoreItem {
    id?: number;
    name?: string;
    storeItemInstanceId?: number;
    groceryLists?: IGroceryList[];
}

export class StoreItem implements IStoreItem {
    constructor(public id?: number, public name?: string, public storeItemInstanceId?: number, public groceryLists?: IGroceryList[]) {}
}
