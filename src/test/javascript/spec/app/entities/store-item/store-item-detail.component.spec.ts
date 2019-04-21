/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Food24TestModule } from '../../../test.module';
import { StoreItemDetailComponent } from 'app/entities/store-item/store-item-detail.component';
import { StoreItem } from 'app/shared/model/store-item.model';

describe('Component Tests', () => {
    describe('StoreItem Management Detail Component', () => {
        let comp: StoreItemDetailComponent;
        let fixture: ComponentFixture<StoreItemDetailComponent>;
        const route = ({ data: of({ storeItem: new StoreItem(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [StoreItemDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(StoreItemDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StoreItemDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.storeItem).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
