import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IFamilyGroup } from 'app/shared/model/family-group.model';
import { FamilyGroupService } from './family-group.service';
import { IFamilyMember } from 'app/shared/model/family-member.model';
import { FamilyMemberService } from 'app/entities/family-member';

@Component({
    selector: 'jhi-family-group-update',
    templateUrl: './family-group-update.component.html'
})
export class FamilyGroupUpdateComponent implements OnInit {
    familyGroup: IFamilyGroup;
    isSaving: boolean;

    familymembers: IFamilyMember[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected familyGroupService: FamilyGroupService,
        protected familyMemberService: FamilyMemberService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ familyGroup }) => {
            this.familyGroup = familyGroup;
        });
        this.familyMemberService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IFamilyMember[]>) => mayBeOk.ok),
                map((response: HttpResponse<IFamilyMember[]>) => response.body)
            )
            .subscribe((res: IFamilyMember[]) => (this.familymembers = res), (res: HttpErrorResponse) => this.onError(res.message));
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

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackFamilyMemberById(index: number, item: IFamilyMember) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}
