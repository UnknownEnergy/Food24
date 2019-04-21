import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IGroceryList } from 'app/shared/model/grocery-list.model';
import { GroceryListService } from './grocery-list.service';
import { IStoreItem } from 'app/shared/model/store-item.model';
import { StoreItemService } from 'app/entities/store-item';
import { IFamilyMember } from 'app/shared/model/family-member.model';
import { FamilyMemberService } from 'app/entities/family-member';
import { IFamilyGroup } from 'app/shared/model/family-group.model';
import { FamilyGroupService } from 'app/entities/family-group';

@Component({
    selector: 'jhi-grocery-list-update',
    templateUrl: './grocery-list-update.component.html'
})
export class GroceryListUpdateComponent implements OnInit {
    groceryList: IGroceryList;
    isSaving: boolean;

    storeitems: IStoreItem[];

    familymembers: IFamilyMember[];

    familygroups: IFamilyGroup[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected groceryListService: GroceryListService,
        protected storeItemService: StoreItemService,
        protected familyMemberService: FamilyMemberService,
        protected familyGroupService: FamilyGroupService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ groceryList }) => {
            this.groceryList = groceryList;
        });
        this.storeItemService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IStoreItem[]>) => mayBeOk.ok),
                map((response: HttpResponse<IStoreItem[]>) => response.body)
            )
            .subscribe((res: IStoreItem[]) => (this.storeitems = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.familyMemberService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IFamilyMember[]>) => mayBeOk.ok),
                map((response: HttpResponse<IFamilyMember[]>) => response.body)
            )
            .subscribe((res: IFamilyMember[]) => (this.familymembers = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.familyGroupService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IFamilyGroup[]>) => mayBeOk.ok),
                map((response: HttpResponse<IFamilyGroup[]>) => response.body)
            )
            .subscribe((res: IFamilyGroup[]) => (this.familygroups = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.groceryList.id !== undefined) {
            this.subscribeToSaveResponse(this.groceryListService.update(this.groceryList));
        } else {
            this.subscribeToSaveResponse(this.groceryListService.create(this.groceryList));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IGroceryList>>) {
        result.subscribe((res: HttpResponse<IGroceryList>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackStoreItemById(index: number, item: IStoreItem) {
        return item.id;
    }

    trackFamilyMemberById(index: number, item: IFamilyMember) {
        return item.id;
    }

    trackFamilyGroupById(index: number, item: IFamilyGroup) {
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
