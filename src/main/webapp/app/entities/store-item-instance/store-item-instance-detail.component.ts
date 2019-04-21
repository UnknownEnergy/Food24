import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IStoreItemInstance } from 'app/shared/model/store-item-instance.model';

@Component({
    selector: 'jhi-store-item-instance-detail',
    templateUrl: './store-item-instance-detail.component.html'
})
export class StoreItemInstanceDetailComponent implements OnInit {
    storeItemInstance: IStoreItemInstance;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ storeItemInstance }) => {
            this.storeItemInstance = storeItemInstance;
        });
    }

    previousState() {
        window.history.back();
    }
}
