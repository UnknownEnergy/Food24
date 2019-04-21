/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Food24TestModule } from '../../../test.module';
import { StoreItemInstanceComponent } from 'app/entities/store-item-instance/store-item-instance.component';
import { StoreItemInstanceService } from 'app/entities/store-item-instance/store-item-instance.service';
import { StoreItemInstance } from 'app/shared/model/store-item-instance.model';

describe('Component Tests', () => {
    describe('StoreItemInstance Management Component', () => {
        let comp: StoreItemInstanceComponent;
        let fixture: ComponentFixture<StoreItemInstanceComponent>;
        let service: StoreItemInstanceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [StoreItemInstanceComponent],
                providers: []
            })
                .overrideTemplate(StoreItemInstanceComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StoreItemInstanceComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StoreItemInstanceService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new StoreItemInstance(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.storeItemInstances[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
