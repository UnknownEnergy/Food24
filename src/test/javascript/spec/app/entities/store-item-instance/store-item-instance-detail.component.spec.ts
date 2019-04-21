/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Food24TestModule } from '../../../test.module';
import { StoreItemInstanceDetailComponent } from 'app/entities/store-item-instance/store-item-instance-detail.component';
import { StoreItemInstance } from 'app/shared/model/store-item-instance.model';

describe('Component Tests', () => {
    describe('StoreItemInstance Management Detail Component', () => {
        let comp: StoreItemInstanceDetailComponent;
        let fixture: ComponentFixture<StoreItemInstanceDetailComponent>;
        const route = ({ data: of({ storeItemInstance: new StoreItemInstance(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [StoreItemInstanceDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(StoreItemInstanceDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StoreItemInstanceDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.storeItemInstance).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
