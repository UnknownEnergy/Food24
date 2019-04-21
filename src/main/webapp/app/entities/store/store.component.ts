import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IStore } from 'app/shared/model/store.model';
import { AccountService } from 'app/core';
import { StoreService } from './store.service';

@Component({
    selector: 'jhi-store',
    templateUrl: './store.component.html'
})
export class StoreComponent implements OnInit, OnDestroy {
    stores: IStore[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected storeService: StoreService,
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
            this.storeService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IStore[]>) => res.ok),
                    map((res: HttpResponse<IStore[]>) => res.body)
                )
                .subscribe((res: IStore[]) => (this.stores = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.storeService
            .query()
            .pipe(
                filter((res: HttpResponse<IStore[]>) => res.ok),
                map((res: HttpResponse<IStore[]>) => res.body)
            )
            .subscribe(
                (res: IStore[]) => {
                    this.stores = res;
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
        this.registerChangeInStores();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IStore) {
        return item.id;
    }

    registerChangeInStores() {
        this.eventSubscriber = this.eventManager.subscribe('storeListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
