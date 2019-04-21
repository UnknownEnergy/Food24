import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { StoreItemInstance } from 'app/shared/model/store-item-instance.model';
import { StoreItemInstanceService } from './store-item-instance.service';
import { StoreItemInstanceComponent } from './store-item-instance.component';
import { StoreItemInstanceDetailComponent } from './store-item-instance-detail.component';
import { StoreItemInstanceUpdateComponent } from './store-item-instance-update.component';
import { StoreItemInstanceDeletePopupComponent } from './store-item-instance-delete-dialog.component';
import { IStoreItemInstance } from 'app/shared/model/store-item-instance.model';

@Injectable({ providedIn: 'root' })
export class StoreItemInstanceResolve implements Resolve<IStoreItemInstance> {
    constructor(private service: StoreItemInstanceService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IStoreItemInstance> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<StoreItemInstance>) => response.ok),
                map((storeItemInstance: HttpResponse<StoreItemInstance>) => storeItemInstance.body)
            );
        }
        return of(new StoreItemInstance());
    }
}

export const storeItemInstanceRoute: Routes = [
    {
        path: '',
        component: StoreItemInstanceComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.storeItemInstance.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: StoreItemInstanceDetailComponent,
        resolve: {
            storeItemInstance: StoreItemInstanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.storeItemInstance.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: StoreItemInstanceUpdateComponent,
        resolve: {
            storeItemInstance: StoreItemInstanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.storeItemInstance.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: StoreItemInstanceUpdateComponent,
        resolve: {
            storeItemInstance: StoreItemInstanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.storeItemInstance.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const storeItemInstancePopupRoute: Routes = [
    {
        path: ':id/delete',
        component: StoreItemInstanceDeletePopupComponent,
        resolve: {
            storeItemInstance: StoreItemInstanceResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.storeItemInstance.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
