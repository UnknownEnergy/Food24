import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { Food24SharedModule } from '../shared';

import { MAP_ROUTE, MapComponent } from './';
import { BrowserModule } from '@angular/platform-browser';
import { AgmCoreModule } from '@agm/core';

@NgModule({
    imports: [
        Food24SharedModule,
        BrowserModule,
        AgmCoreModule.forRoot({
            //TODO ADD API KEY HERE!!!
            apiKey: 'AIzaSyDvF652TU9vT3hNw_YkMOvM2RDshzAOmUU'
        }),
        RouterModule.forRoot([MAP_ROUTE], { useHash: true })
    ],
    declarations: [MapComponent],
    bootstrap: [MapComponent],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class Food24AppMapModule {}
