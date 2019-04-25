import { IGroceryList } from 'app/shared/model/grocery-list.model';
import { IFamilyGroup } from 'app/shared/model/family-group.model';

export interface IFamilyMember {
    id?: number;
    groceryLists?: IGroceryList[];
    userId?: number;
    familyGroups?: IFamilyGroup[];
    locationId?: number;
}

export class FamilyMember implements IFamilyMember {
    constructor(
        public id?: number,
        public groceryLists?: IGroceryList[],
        public userId?: number,
        public familyGroups?: IFamilyGroup[],
        public locationId?: number
    ) {}
}
