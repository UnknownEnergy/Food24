import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IFamilyGroup } from 'app/shared/model/family-group.model';
import { AccountService } from 'app/core';
import { FamilyGroupService } from './family-group.service';

@Component({
    selector: 'jhi-family-group',
    templateUrl: './family-group.component.html'
})
export class FamilyGroupComponent implements OnInit, OnDestroy {
    familyGroups: IFamilyGroup[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected familyGroupService: FamilyGroupService,
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
            this.familyGroupService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IFamilyGroup[]>) => res.ok),
                    map((res: HttpResponse<IFamilyGroup[]>) => res.body)
                )
                .subscribe((res: IFamilyGroup[]) => (this.familyGroups = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.familyGroupService
            .query()
            .pipe(
                filter((res: HttpResponse<IFamilyGroup[]>) => res.ok),
                map((res: HttpResponse<IFamilyGroup[]>) => res.body)
            )
            .subscribe(
                (res: IFamilyGroup[]) => {
                    this.familyGroups = res;
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
        this.registerChangeInFamilyGroups();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFamilyGroup) {
        return item.id;
    }

    registerChangeInFamilyGroups() {
        this.eventSubscriber = this.eventManager.subscribe('familyGroupListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
