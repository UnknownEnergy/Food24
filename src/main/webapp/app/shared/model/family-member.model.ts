import { IGroceryList } from 'app/shared/model/grocery-list.model';
import { IFamilyGroup } from 'app/shared/model/family-group.model';

export interface IFamilyMember {
    id?: number;
    userName?: string;
    firstName?: string;
    lastName?: string;
    groceryLists?: IGroceryList[];
    familyGroups?: IFamilyGroup[];
    locationId?: number;
}

export class FamilyMember implements IFamilyMember {
    constructor(
        public id?: number,
        public userName?: string,
        public firstName?: string,
        public lastName?: string,
        public groceryLists?: IGroceryList[],
        public familyGroups?: IFamilyGroup[],
        public locationId?: number
    ) {}
}
