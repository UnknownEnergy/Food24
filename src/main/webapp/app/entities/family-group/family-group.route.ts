import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FamilyGroup } from 'app/shared/model/family-group.model';
import { FamilyGroupService } from './family-group.service';
import { FamilyGroupComponent } from './family-group.component';
import { FamilyGroupDetailComponent } from './family-group-detail.component';
import { FamilyGroupUpdateComponent } from './family-group-update.component';
import { FamilyGroupDeletePopupComponent } from './family-group-delete-dialog.component';
import { IFamilyGroup } from 'app/shared/model/family-group.model';

@Injectable({ providedIn: 'root' })
export class FamilyGroupResolve implements Resolve<IFamilyGroup> {
    constructor(private service: FamilyGroupService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFamilyGroup> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<FamilyGroup>) => response.ok),
                map((familyGroup: HttpResponse<FamilyGroup>) => familyGroup.body)
            );
        }
        return of(new FamilyGroup());
    }
}

export const familyGroupRoute: Routes = [
    {
        path: '',
        component: FamilyGroupComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.familyGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: FamilyGroupDetailComponent,
        resolve: {
            familyGroup: FamilyGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.familyGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: FamilyGroupUpdateComponent,
        resolve: {
            familyGroup: FamilyGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.familyGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: FamilyGroupUpdateComponent,
        resolve: {
            familyGroup: FamilyGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.familyGroup.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const familyGroupPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: FamilyGroupDeletePopupComponent,
        resolve: {
            familyGroup: FamilyGroupResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.familyGroup.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
