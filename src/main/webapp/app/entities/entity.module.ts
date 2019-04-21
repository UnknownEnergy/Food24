import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
    imports: [
        RouterModule.forChild([
            {
                path: 'family-group',
                loadChildren: './family-group/family-group.module#Food24FamilyGroupModule'
            },
            {
                path: 'grocery-list',
                loadChildren: './grocery-list/grocery-list.module#Food24GroceryListModule'
            },
            {
                path: 'store-item',
                loadChildren: './store-item/store-item.module#Food24StoreItemModule'
            },
            {
                path: 'store-item-instance',
                loadChildren: './store-item-instance/store-item-instance.module#Food24StoreItemInstanceModule'
            },
            {
                path: 'store',
                loadChildren: './store/store.module#Food24StoreModule'
            },
            {
                path: 'location',
                loadChildren: './location/location.module#Food24LocationModule'
            },
            {
                path: 'family-member',
                loadChildren: './family-member/family-member.module#Food24FamilyMemberModule'
            }
            /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
        ])
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Food24EntityModule {}
