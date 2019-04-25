import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IFamilyGroup } from 'app/shared/model/family-group.model';
import { FamilyGroupService } from './family-group.service';

@Component({
    selector: 'jhi-family-group-update',
    templateUrl: './family-group-update.component.html'
})
export class FamilyGroupUpdateComponent implements OnInit {
    familyGroup: IFamilyGroup;
    isSaving: boolean;

    constructor(protected familyGroupService: FamilyGroupService, protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ familyGroup }) => {
            this.familyGroup = familyGroup;
        });
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.familyGroup.id !== undefined) {
            this.subscribeToSaveResponse(this.familyGroupService.update(this.familyGroup));
        } else {
            this.subscribeToSaveResponse(this.familyGroupService.create(this.familyGroup));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamilyGroup>>) {
        result.subscribe((res: HttpResponse<IFamilyGroup>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }
}
