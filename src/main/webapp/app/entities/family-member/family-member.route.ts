import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { FamilyMember } from 'app/shared/model/family-member.model';
import { FamilyMemberService } from './family-member.service';
import { FamilyMemberComponent } from './family-member.component';
import { FamilyMemberDetailComponent } from './family-member-detail.component';
import { FamilyMemberUpdateComponent } from './family-member-update.component';
import { FamilyMemberDeletePopupComponent } from './family-member-delete-dialog.component';
import { IFamilyMember } from 'app/shared/model/family-member.model';

@Injectable({ providedIn: 'root' })
export class FamilyMemberResolve implements Resolve<IFamilyMember> {
    constructor(private service: FamilyMemberService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IFamilyMember> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<FamilyMember>) => response.ok),
                map((familyMember: HttpResponse<FamilyMember>) => familyMember.body)
            );
        }
        return of(new FamilyMember());
    }
}

export const familyMemberRoute: Routes = [
    {
        path: '',
        component: FamilyMemberComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.familyMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: FamilyMemberDetailComponent,
        resolve: {
            familyMember: FamilyMemberResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.familyMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: FamilyMemberUpdateComponent,
        resolve: {
            familyMember: FamilyMemberResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.familyMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: FamilyMemberUpdateComponent,
        resolve: {
            familyMember: FamilyMemberResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.familyMember.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const familyMemberPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: FamilyMemberDeletePopupComponent,
        resolve: {
            familyMember: FamilyMemberResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'food24App.familyMember.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
