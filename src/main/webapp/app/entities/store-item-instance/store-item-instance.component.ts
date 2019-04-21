import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IStoreItemInstance } from 'app/shared/model/store-item-instance.model';
import { AccountService } from 'app/core';
import { StoreItemInstanceService } from './store-item-instance.service';

@Component({
    selector: 'jhi-store-item-instance',
    templateUrl: './store-item-instance.component.html'
})
export class StoreItemInstanceComponent implements OnInit, OnDestroy {
    storeItemInstances: IStoreItemInstance[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected storeItemInstanceService: StoreItemInstanceService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService
    ) {
        this.currentSearch =
            this.activatedRoute.snapshot && this.activatedRoute.snapshot.params['search']
                ? this.activatedRoute.snapshot.params['search']
                : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.storeItemInstanceService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IStoreItemInstance[]>) => res.ok),
                    map((res: HttpResponse<IStoreItemInstance[]>) => res.body)
                )
                .subscribe(
                    (res: IStoreItemInstance[]) => (this.storeItemInstances = res),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            return;
        }
        this.storeItemInstanceService
            .query()
            .pipe(
                filter((res: HttpResponse<IStoreItemInstance[]>) => res.ok),
                map((res: HttpResponse<IStoreItemInstance[]>) => res.body)
            )
            .subscribe(
                (res: IStoreItemInstance[]) => {
                    this.storeItemInstances = res;
                    this.currentSearch = '';
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }

    ngOnInit() {
        this.loadAll();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInStoreItemInstances();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IStoreItemInstance) {
        return item.id;
    }

    registerChangeInStoreItemInstances() {
        this.eventSubscriber = this.eventManager.subscribe('storeItemInstanceListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
