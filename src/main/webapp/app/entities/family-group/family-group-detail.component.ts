import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFamilyGroup } from 'app/shared/model/family-group.model';

@Component({
    selector: 'jhi-family-group-detail',
    templateUrl: './family-group-detail.component.html'
})
export class FamilyGroupDetailComponent implements OnInit {
    familyGroup: IFamilyGroup;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ familyGroup }) => {
            this.familyGroup = familyGroup;
        });
    }

    previousState() {
        window.history.back();
    }
}
