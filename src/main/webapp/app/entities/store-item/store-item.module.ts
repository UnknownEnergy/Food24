import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { Food24SharedModule } from 'app/shared';
import {
    StoreItemComponent,
    StoreItemDetailComponent,
    StoreItemUpdateComponent,
    StoreItemDeletePopupComponent,
    StoreItemDeleteDialogComponent,
    storeItemRoute,
    storeItemPopupRoute
} from './';

const ENTITY_STATES = [...storeItemRoute, ...storeItemPopupRoute];

@NgModule({
    imports: [Food24SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        StoreItemComponent,
        StoreItemDetailComponent,
        StoreItemUpdateComponent,
        StoreItemDeleteDialogComponent,
        StoreItemDeletePopupComponent
    ],
    entryComponents: [StoreItemComponent, StoreItemUpdateComponent, StoreItemDeleteDialogComponent, StoreItemDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Food24StoreItemModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
