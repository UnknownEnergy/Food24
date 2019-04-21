/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Food24TestModule } from '../../../test.module';
import { GroceryListDetailComponent } from 'app/entities/grocery-list/grocery-list-detail.component';
import { GroceryList } from 'app/shared/model/grocery-list.model';

describe('Component Tests', () => {
    describe('GroceryList Management Detail Component', () => {
        let comp: GroceryListDetailComponent;
        let fixture: ComponentFixture<GroceryListDetailComponent>;
        const route = ({ data: of({ groceryList: new GroceryList(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [GroceryListDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(GroceryListDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(GroceryListDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.groceryList).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
