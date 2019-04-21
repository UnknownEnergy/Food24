import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGroceryList } from 'app/shared/model/grocery-list.model';

@Component({
    selector: 'jhi-grocery-list-detail',
    templateUrl: './grocery-list-detail.component.html'
})
export class GroceryListDetailComponent implements OnInit {
    groceryList: IGroceryList;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ groceryList }) => {
            this.groceryList = groceryList;
        });
    }

    previousState() {
        window.history.back();
    }
}
