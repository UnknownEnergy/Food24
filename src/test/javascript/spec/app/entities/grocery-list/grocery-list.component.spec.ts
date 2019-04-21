/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Food24TestModule } from '../../../test.module';
import { GroceryListComponent } from 'app/entities/grocery-list/grocery-list.component';
import { GroceryListService } from 'app/entities/grocery-list/grocery-list.service';
import { GroceryList } from 'app/shared/model/grocery-list.model';

describe('Component Tests', () => {
    describe('GroceryList Management Component', () => {
        let comp: GroceryListComponent;
        let fixture: ComponentFixture<GroceryListComponent>;
        let service: GroceryListService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [GroceryListComponent],
                providers: []
            })
                .overrideTemplate(GroceryListComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(GroceryListComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GroceryListService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new GroceryList(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.groceryLists[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
