import { IStoreItem } from 'app/shared/model/store-item.model';

export interface IGroceryList {
    id?: number;
    name?: string;
    storeItems?: IStoreItem[];
    familyMemberId?: number;
    familyGroupId?: number;
}

export class GroceryList implements IGroceryList {
    constructor(
        public id?: number,
        public name?: string,
        public storeItems?: IStoreItem[],
        public familyMemberId?: number,
        public familyGroupId?: number
    ) {}
}
