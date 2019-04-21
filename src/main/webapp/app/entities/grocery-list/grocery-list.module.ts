import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { Food24SharedModule } from 'app/shared';
import {
    GroceryListComponent,
    GroceryListDetailComponent,
    GroceryListUpdateComponent,
    GroceryListDeletePopupComponent,
    GroceryListDeleteDialogComponent,
    groceryListRoute,
    groceryListPopupRoute
} from './';

const ENTITY_STATES = [...groceryListRoute, ...groceryListPopupRoute];

@NgModule({
    imports: [Food24SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        GroceryListComponent,
        GroceryListDetailComponent,
        GroceryListUpdateComponent,
        GroceryListDeleteDialogComponent,
        GroceryListDeletePopupComponent
    ],
    entryComponents: [GroceryListComponent, GroceryListUpdateComponent, GroceryListDeleteDialogComponent, GroceryListDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Food24GroceryListModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
