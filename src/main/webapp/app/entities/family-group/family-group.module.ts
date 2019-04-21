import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { Food24SharedModule } from 'app/shared';
import {
    FamilyGroupComponent,
    FamilyGroupDetailComponent,
    FamilyGroupUpdateComponent,
    FamilyGroupDeletePopupComponent,
    FamilyGroupDeleteDialogComponent,
    familyGroupRoute,
    familyGroupPopupRoute
} from './';

const ENTITY_STATES = [...familyGroupRoute, ...familyGroupPopupRoute];

@NgModule({
    imports: [Food24SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FamilyGroupComponent,
        FamilyGroupDetailComponent,
        FamilyGroupUpdateComponent,
        FamilyGroupDeleteDialogComponent,
        FamilyGroupDeletePopupComponent
    ],
    entryComponents: [FamilyGroupComponent, FamilyGroupUpdateComponent, FamilyGroupDeleteDialogComponent, FamilyGroupDeletePopupComponent],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Food24FamilyGroupModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
