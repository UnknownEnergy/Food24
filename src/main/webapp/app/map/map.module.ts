import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Food24SharedModule } from '../shared';

import { MAP_ROUTE, MapComponent } from './';

@NgModule({
    imports: [Food24SharedModule, RouterModule.forRoot([MAP_ROUTE], { useHash: true })],
    declarations: [MapComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Food24AppMapModule {}
