import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStoreItem } from 'app/shared/model/store-item.model';

@Component({
    selector: 'jhi-store-item-detail',
    templateUrl: './store-item-detail.component.html'
})
export class StoreItemDetailComponent implements OnInit {
    storeItem: IStoreItem;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ storeItem }) => {
            this.storeItem = storeItem;
        });
    }

    previousState() {
        window.history.back();
    }
}
