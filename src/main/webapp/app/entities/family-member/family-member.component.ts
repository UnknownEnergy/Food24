import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IFamilyMember } from 'app/shared/model/family-member.model';
import { AccountService } from 'app/core';
import { FamilyMemberService } from './family-member.service';

@Component({
    selector: 'jhi-family-member',
    templateUrl: './family-member.component.html'
})
export class FamilyMemberComponent implements OnInit, OnDestroy {
    familyMembers: IFamilyMember[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        protected familyMemberService: FamilyMemberService,
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
            this.familyMemberService
                .search({
                    query: this.currentSearch
                })
                .pipe(
                    filter((res: HttpResponse<IFamilyMember[]>) => res.ok),
                    map((res: HttpResponse<IFamilyMember[]>) => res.body)
                )
                .subscribe((res: IFamilyMember[]) => (this.familyMembers = res), (res: HttpErrorResponse) => this.onError(res.message));
            return;
        }
        this.familyMemberService
            .query()
            .pipe(
                filter((res: HttpResponse<IFamilyMember[]>) => res.ok),
                map((res: HttpResponse<IFamilyMember[]>) => res.body)
            )
            .subscribe(
                (res: IFamilyMember[]) => {
                    this.familyMembers = res;
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
        this.registerChangeInFamilyMembers();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: IFamilyMember) {
        return item.id;
    }

    registerChangeInFamilyMembers() {
        this.eventSubscriber = this.eventManager.subscribe('familyMemberListModification', response => this.loadAll());
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
