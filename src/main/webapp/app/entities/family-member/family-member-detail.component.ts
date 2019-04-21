import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFamilyMember } from 'app/shared/model/family-member.model';

@Component({
    selector: 'jhi-family-member-detail',
    templateUrl: './family-member-detail.component.html'
})
export class FamilyMemberDetailComponent implements OnInit {
    familyMember: IFamilyMember;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ familyMember }) => {
            this.familyMember = familyMember;
        });
    }

    previousState() {
        window.history.back();
    }
}
