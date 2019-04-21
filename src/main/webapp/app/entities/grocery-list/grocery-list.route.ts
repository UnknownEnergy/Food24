import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { GroceryList } from 'app/shared/model/grocery-list.model';
import { GroceryListService } from './grocery-list.service';
import { GroceryListComponent } from './grocery-list.component';
import { GroceryListDetailComponent } from './grocery-list-detail.component';
import { GroceryListUpdateComponent } from './grocery-list-update.component';
import { GroceryListDeletePopupComponent } from './grocery-list-delete-dialog.component';
import { IGroceryList } from 'app/shared/model/grocery-list.model';

@Injectable({ providedIn: 'root' })
export class GroceryListResolve implements Resolve<IGroceryList> {
    constructor(private service: GroceryListService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IGroceryList> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<GroceryList>) => response.ok),
                map((groceryList: HttpResponse<GroceryList>) => groceryList.body)
            );
        }
        return of(new GroceryList());
    }
}

export const groceryListRoute: Routes = [
    {
        path: '',
        component: GroceryListComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.groceryList.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: GroceryListDetailComponent,
        resolve: {
            groceryList: GroceryListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.groceryList.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: GroceryListUpdateComponent,
        resolve: {
            groceryList: GroceryListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.groceryList.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: GroceryListUpdateComponent,
        resolve: {
            groceryList: GroceryListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.groceryList.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const groceryListPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: GroceryListDeletePopupComponent,
        resolve: {
            groceryList: GroceryListResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.groceryList.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
