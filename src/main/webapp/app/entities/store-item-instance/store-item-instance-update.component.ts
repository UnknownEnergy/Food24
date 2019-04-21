import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IStoreItemInstance } from 'app/shared/model/store-item-instance.model';
import { StoreItemInstanceService } from './store-item-instance.service';
import { IStoreItem } from 'app/shared/model/store-item.model';
import { StoreItemService } from 'app/entities/store-item';
import { IStore } from 'app/shared/model/store.model';
import { StoreService } from 'app/entities/store';

@Component({
    selector: 'jhi-store-item-instance-update',
    templateUrl: './store-item-instance-update.component.html'
})
export class StoreItemInstanceUpdateComponent implements OnInit {
    storeItemInstance: IStoreItemInstance;
    isSaving: boolean;

    storeitems: IStoreItem[];

    stores: IStore[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected storeItemInstanceService: StoreItemInstanceService,
        protected storeItemService: StoreItemService,
        protected storeService: StoreService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ storeItemInstance }) => {
            this.storeItemInstance = storeItemInstance;
        });
        this.storeItemService
            .query({ filter: 'storeiteminstance-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<IStoreItem[]>) => mayBeOk.ok),
                map((response: HttpResponse<IStoreItem[]>) => response.body)
            )
            .subscribe(
                (res: IStoreItem[]) => {
                    if (!this.storeItemInstance.storeItemId) {
                        this.storeitems = res;
                    } else {
                        this.storeItemService
                            .find(this.storeItemInstance.storeItemId)
                            .pipe(
                                filter((subResMayBeOk: HttpResponse<IStoreItem>) => subResMayBeOk.ok),
                                map((subResponse: HttpResponse<IStoreItem>) => subResponse.body)
                            )
                            .subscribe(
                                (subRes: IStoreItem) => (this.storeitems = [subRes].concat(res)),
                                (subRes: HttpErrorResponse) => this.onError(subRes.message)
                            );
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.storeService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IStore[]>) => mayBeOk.ok),
                map((response: HttpResponse<IStore[]>) => response.body)
            )
            .subscribe((res: IStore[]) => (this.stores = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.storeItemInstance.id !== undefined) {
            this.subscribeToSaveResponse(this.storeItemInstanceService.update(this.storeItemInstance));
        } else {
            this.subscribeToSaveResponse(this.storeItemInstanceService.create(this.storeItemInstance));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IStoreItemInstance>>) {
        result.subscribe((res: HttpResponse<IStoreItemInstance>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackStoreById(index: number, item: IStore) {
        return item.id;
    }
}
