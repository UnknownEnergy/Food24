import { IGroceryList } from 'app/shared/model/grocery-list.model';
import { IFamilyMember } from 'app/shared/model/family-member.model';

export interface IFamilyGroup {
    id?: number;
    name?: string;
    groceryLists?: IGroceryList[];
    familyMembers?: IFamilyMember[];
}

export class FamilyGroup implements IFamilyGroup {
    constructor(public id?: number, public name?: string, public groceryLists?: IGroceryList[], public familyMembers?: IFamilyMember[]) {}
}
