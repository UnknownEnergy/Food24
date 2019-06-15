import { Component, OnInit } from '@angular/core';
import { ILocation } from 'app/shared/model/location.model';
import { filter, map } from 'rxjs/operators';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { LocationService } from 'app/entities/location';
import { JhiAlertService } from 'ng-jhipster';
import { IStoreItemInstance } from 'app/shared/model/store-item-instance.model';
import { StoreItemInstanceService } from 'app/entities/store-item-instance';

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
    storeItemInstances: IStoreItemInstance[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected locationService: LocationService,
        protected storeItemInstanceService: StoreItemInstanceService
    ) {
        if (navigator) {
            navigator.geolocation.getCurrentPosition(pos => {
                this.lng = +pos.coords.longitude;
                this.lat = +pos.coords.latitude;
            });
        }
        this.title = 'Look for your store nearby...';
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

        this.storeItemInstanceService
            .query()
            .pipe(
                filter((res: HttpResponse<IStoreItemInstance[]>) => res.ok),
                map((res: HttpResponse<IStoreItemInstance[]>) => res.body)
            )
            .subscribe(
                (res: IStoreItemInstance[]) => {
                    this.storeItemInstances = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
