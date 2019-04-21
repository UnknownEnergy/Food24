import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IStoreItem } from 'app/shared/model/store-item.model';
import { StoreItemService } from './store-item.service';
import { IStoreItemInstance } from 'app/shared/model/store-item-instance.model';
import { StoreItemInstanceService } from 'app/entities/store-item-instance';
import { IGroceryList } from 'app/shared/model/grocery-list.model';
import { GroceryListService } from 'app/entities/grocery-list';

@Component({
    selector: 'jhi-store-item-update',
    templateUrl: './store-item-update.component.html'
})
export class StoreItemUpdateComponent implements OnInit {
    storeItem: IStoreItem;
    isSaving: boolean;

    storeiteminstances: IStoreItemInstance[];

    grocerylists: IGroceryList[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected storeItemService: StoreItemService,
        protected storeItemInstanceService: StoreItemInstanceService,
        protected groceryListService: GroceryListService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ storeItem }) => {
            this.storeItem = storeItem;
        });
        this.storeItemInstanceService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IStoreItemInstance[]>) => mayBeOk.ok),
                map((response: HttpResponse<IStoreItemInstance[]>) => response.body)
            )
            .subscribe(
                (res: IStoreItemInstance[]) => (this.storeiteminstances = res),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.groceryListService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IGroceryList[]>) => mayBeOk.ok),
                map((response: HttpResponse<IGroceryList[]>) => response.body)
            )
            .subscribe((res: IGroceryList[]) => (this.grocerylists = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.storeItem.id !== undefined) {
            this.subscribeToSaveResponse(this.storeItemService.update(this.storeItem));
        } else {
            this.subscribeToSaveResponse(this.storeItemService.create(this.storeItem));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IStoreItem>>) {
        result.subscribe((res: HttpResponse<IStoreItem>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackStoreItemInstanceById(index: number, item: IStoreItemInstance) {
        return item.id;
    }

    trackGroceryListById(index: number, item: IGroceryList) {
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
