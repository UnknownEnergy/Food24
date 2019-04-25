import { IStoreItem } from 'app/shared/model/store-item.model';

export interface IGroceryList {
    id?: number;
    name?: string;
    storeItems?: IStoreItem[];
    familyGroupName?: string;
    familyGroupId?: number;
    familyMemberId?: number;
}

export class GroceryList implements IGroceryList {
    constructor(
        public id?: number,
        public name?: string,
        public storeItems?: IStoreItem[],
        public familyGroupName?: string,
        public familyGroupId?: number,
        public familyMemberId?: number
    ) {}
}
