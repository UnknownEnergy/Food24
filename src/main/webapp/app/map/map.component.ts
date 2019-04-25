import { Component, OnInit } from '@angular/core';
import { ILocation } from 'app/shared/model/location.model';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { LocationService } from 'app/entities/location';
import { JhiAlertService } from 'ng-jhipster';

@Component({
    selector: 'jhi-map',
    templateUrl: './map.component.html',
    styleUrls: ['map.component.css']
})
export class MapComponent implements OnInit {
    title: string;
    lat: number;
    lng: number;
    locations: ILocation[];

    constructor(protected jhiAlertService: JhiAlertService, protected locationService: LocationService) {
        if (navigator) {
            navigator.geolocation.getCurrentPosition(pos => {
                this.lng = +pos.coords.longitude;
                this.lat = +pos.coords.latitude;
            });
        }
        this.title = 'Maps';
    }
    ngOnInit() {
        this.loadAll();
    }

    loadAll() {
        this.locationService
            .query()
            .pipe(
                filter((res: HttpResponse<ILocation[]>) => res.ok),
                map((res: HttpResponse<ILocation[]>) => res.body)
            )
            .subscribe(
                (res: ILocation[]) => {
                    this.locations = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
