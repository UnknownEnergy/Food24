import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { Food24SharedModule } from 'app/shared';
import {
    StoreItemInstanceComponent,
    StoreItemInstanceDetailComponent,
    StoreItemInstanceUpdateComponent,
    StoreItemInstanceDeletePopupComponent,
    StoreItemInstanceDeleteDialogComponent,
    storeItemInstanceRoute,
    storeItemInstancePopupRoute
} from './';

const ENTITY_STATES = [...storeItemInstanceRoute, ...storeItemInstancePopupRoute];

@NgModule({
    imports: [Food24SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StoreItemInstanceComponent,
        StoreItemInstanceDetailComponent,
        StoreItemInstanceUpdateComponent,
        StoreItemInstanceDeleteDialogComponent,
        StoreItemInstanceDeletePopupComponent
    ],
    entryComponents: [
        StoreItemInstanceComponent,
        StoreItemInstanceUpdateComponent,
        StoreItemInstanceDeleteDialogComponent,
        StoreItemInstanceDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Food24StoreItemInstanceModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
