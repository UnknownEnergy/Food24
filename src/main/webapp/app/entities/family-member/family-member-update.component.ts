import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IFamilyMember } from 'app/shared/model/family-member.model';
import { FamilyMemberService } from './family-member.service';
import { IFamilyGroup } from 'app/shared/model/family-group.model';
import { FamilyGroupService } from 'app/entities/family-group';
import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from 'app/entities/location';

@Component({
    selector: 'jhi-family-member-update',
    templateUrl: './family-member-update.component.html'
})
export class FamilyMemberUpdateComponent implements OnInit {
    familyMember: IFamilyMember;
    isSaving: boolean;

    familygroups: IFamilyGroup[];

    locations: ILocation[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected familyMemberService: FamilyMemberService,
        protected familyGroupService: FamilyGroupService,
        protected locationService: LocationService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ familyMember }) => {
            this.familyMember = familyMember;
        });
        this.familyGroupService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IFamilyGroup[]>) => mayBeOk.ok),
                map((response: HttpResponse<IFamilyGroup[]>) => response.body)
            )
            .subscribe((res: IFamilyGroup[]) => (this.familygroups = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.locationService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ILocation[]>) => mayBeOk.ok),
                map((response: HttpResponse<ILocation[]>) => response.body)
            )
            .subscribe((res: ILocation[]) => (this.locations = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.familyMember.id !== undefined) {
            this.subscribeToSaveResponse(this.familyMemberService.update(this.familyMember));
        } else {
            this.subscribeToSaveResponse(this.familyMemberService.create(this.familyMember));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IFamilyMember>>) {
        result.subscribe((res: HttpResponse<IFamilyMember>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackFamilyGroupById(index: number, item: IFamilyGroup) {
        return item.id;
    }

    trackLocationById(index: number, item: ILocation) {
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
