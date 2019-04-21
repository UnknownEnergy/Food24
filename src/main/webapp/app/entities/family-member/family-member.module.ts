import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { Food24SharedModule } from 'app/shared';
import {
    FamilyMemberComponent,
    FamilyMemberDetailComponent,
    FamilyMemberUpdateComponent,
    FamilyMemberDeletePopupComponent,
    FamilyMemberDeleteDialogComponent,
    familyMemberRoute,
    familyMemberPopupRoute
} from './';

const ENTITY_STATES = [...familyMemberRoute, ...familyMemberPopupRoute];

@NgModule({
    imports: [Food24SharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FamilyMemberComponent,
        FamilyMemberDetailComponent,
        FamilyMemberUpdateComponent,
        FamilyMemberDeleteDialogComponent,
        FamilyMemberDeletePopupComponent
    ],
    entryComponents: [
        FamilyMemberComponent,
        FamilyMemberUpdateComponent,
        FamilyMemberDeleteDialogComponent,
        FamilyMemberDeletePopupComponent
    ],
    providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Food24FamilyMemberModule {
    constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
        this.languageHelper.language.subscribe((languageKey: string) => {
            if (languageKey !== undefined) {
                this.languageService.changeLanguage(languageKey);
            }
        });
    }
}
