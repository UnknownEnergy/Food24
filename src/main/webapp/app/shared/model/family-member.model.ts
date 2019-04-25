import { IGroceryList } from 'app/shared/model/grocery-list.model';
import { IFamilyGroup } from 'app/shared/model/family-group.model';

export interface IFamilyMember {
    id?: number;
    groceryLists?: IGroceryList[];
    userLogin?: string;
    userId?: number;
    familyGroups?: IFamilyGroup[];
    locationDescription?: string;
    locationId?: number;
}

export class FamilyMember implements IFamilyMember {
    constructor(
        public id?: number,
        public groceryLists?: IGroceryList[],
        public userLogin?: string,
        public userId?: number,
        public familyGroups?: IFamilyGroup[],
        public locationDescription?: string,
        public locationId?: number
    ) {}
}
