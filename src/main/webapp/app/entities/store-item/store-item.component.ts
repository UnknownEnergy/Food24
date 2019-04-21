import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IStoreItem } from 'app/shared/model/store-item.model';
import { AccountService } from 'app/core';
import { StoreItemService } from './store-item.service';

@Component({
    selector: 'jhi-store-item',
    templateUrl: './store-item.component.html'
})
export class StoreItemComponent implements OnInit, OnDestroy {
    storeItems: IStoreItem[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected storeItemService: StoreItemService,
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
            this.storeItemService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IStoreItem[]>) => res.ok),
                    map((res: HttpResponse<IStoreItem[]>) => res.body)
                )
                .subscribe((res: IStoreItem[]) => (this.storeItems = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.storeItemService
            .query()
            .pipe(
                filter((res: HttpResponse<IStoreItem[]>) => res.ok),
                map((res: HttpResponse<IStoreItem[]>) => res.body)
            )
            .subscribe(
                (res: IStoreItem[]) => {
                    this.storeItems = res;
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
        this.registerChangeInStoreItems();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IStoreItem) {
        return item.id;
    }

    registerChangeInStoreItems() {
        this.eventSubscriber = this.eventManager.subscribe('storeItemListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
