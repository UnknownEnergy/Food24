import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StoreItem } from 'app/shared/model/store-item.model';
import { StoreItemService } from './store-item.service';
import { StoreItemComponent } from './store-item.component';
import { StoreItemDetailComponent } from './store-item-detail.component';
import { StoreItemUpdateComponent } from './store-item-update.component';
import { StoreItemDeletePopupComponent } from './store-item-delete-dialog.component';
import { IStoreItem } from 'app/shared/model/store-item.model';

@Injectable({ providedIn: 'root' })
export class StoreItemResolve implements Resolve<IStoreItem> {
    constructor(private service: StoreItemService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStoreItem> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<StoreItem>) => response.ok),
                map((storeItem: HttpResponse<StoreItem>) => storeItem.body)
            );
        }
        return of(new StoreItem());
    }
}

export const storeItemRoute: Routes = [
    {
        path: '',
        component: StoreItemComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.storeItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: StoreItemDetailComponent,
        resolve: {
            storeItem: StoreItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.storeItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: StoreItemUpdateComponent,
        resolve: {
            storeItem: StoreItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.storeItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: StoreItemUpdateComponent,
        resolve: {
            storeItem: StoreItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.storeItem.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const storeItemPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: StoreItemDeletePopupComponent,
        resolve: {
            storeItem: StoreItemResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.storeItem.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
