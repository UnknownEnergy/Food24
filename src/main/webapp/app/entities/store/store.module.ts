import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { Food24SharedModule } from 'app/shared';
import {
    StoreComponent,
    StoreDetailComponent,
    StoreUpdateComponent,
    StoreDeletePopupComponent,
    StoreDeleteDialogComponent,
    storeRoute,
    storePopupRoute
} from './';

const ENTITY_STATES = [...storeRoute, ...storePopupRoute];

@NgModule({
    imports: [Food24SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [StoreComponent, StoreDetailComponent, StoreUpdateComponent, StoreDeleteDialogComponent, StoreDeletePopupComponent],
    entryComponents: [StoreComponent, StoreUpdateComponent, StoreDeleteDialogComponent, StoreDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Food24StoreModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
