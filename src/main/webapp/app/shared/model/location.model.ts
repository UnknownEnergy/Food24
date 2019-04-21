import { IFamilyMember } from 'app/shared/model/family-member.model';
import { IStore } from 'app/shared/model/store.model';

export interface ILocation {
    id?: number;
    latitude?: number;
    longitude?: number;
    description?: string;
    familyMembers?: IFamilyMember[];
    stores?: IStore[];
}

export class Location implements ILocation {
    constructor(
        public id?: number,
        public latitude?: number,
        public longitude?: number,
        public description?: string,
        public familyMembers?: IFamilyMember[],
        public stores?: IStore[]
    ) {}
}
