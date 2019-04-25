import { Component, OnInit } from '@angular/core';

@Component({
    selector: 'jhi-map',
    templateUrl: './map.component.html',
    styleUrls: ['map.component.css']
})
export class MapComponent implements OnInit {
    title: string;
    lat: number = 47.073148;
    lng: number = 15.417011;

    constructor() {
        this.title = 'MapComponent message';
    }

    ngOnInit() {}
}
