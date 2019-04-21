import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IGroceryList } from 'app/shared/model/grocery-list.model';
import { AccountService } from 'app/core';
import { GroceryListService } from './grocery-list.service';

@Component({
    selector: 'jhi-grocery-list',
    templateUrl: './grocery-list.component.html'
})
export class GroceryListComponent implements OnInit, OnDestroy {
    groceryLists: IGroceryList[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected groceryListService: GroceryListService,
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
            this.groceryListService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IGroceryList[]>) => res.ok),
                    map((res: HttpResponse<IGroceryList[]>) => res.body)
                )
                .subscribe((res: IGroceryList[]) => (this.groceryLists = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.groceryListService
            .query()
            .pipe(
                filter((res: HttpResponse<IGroceryList[]>) => res.ok),
                map((res: HttpResponse<IGroceryList[]>) => res.body)
            )
            .subscribe(
                (res: IGroceryList[]) => {
                    this.groceryLists = res;
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
        this.registerChangeInGroceryLists();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IGroceryList) {
        return item.id;
    }

    registerChangeInGroceryLists() {
        this.eventSubscriber = this.eventManager.subscribe('groceryListListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
