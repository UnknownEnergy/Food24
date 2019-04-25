import { IGroceryList } from 'app/shared/model/grocery-list.model';

export interface IFamilyGroup {
    id?: number;
    name?: string;
    groceryLists?: IGroceryList[];
}

export class FamilyGroup implements IFamilyGroup {
    constructor(public id?: number, public name?: string, public groceryLists?: IGroceryList[]) {}
}
