/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Food24TestModule } from '../../../test.module';
import { StoreItemComponent } from 'app/entities/store-item/store-item.component';
import { StoreItemService } from 'app/entities/store-item/store-item.service';
import { StoreItem } from 'app/shared/model/store-item.model';

describe('Component Tests', () => {
    describe('StoreItem Management Component', () => {
        let comp: StoreItemComponent;
        let fixture: ComponentFixture<StoreItemComponent>;
        let service: StoreItemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [StoreItemComponent],
                providers: []
            })
                .overrideTemplate(StoreItemComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StoreItemComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StoreItemService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new StoreItem(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.storeItems[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
