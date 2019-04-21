/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Food24TestModule } from '../../../test.module';
import { GroceryListUpdateComponent } from 'app/entities/grocery-list/grocery-list-update.component';
import { GroceryListService } from 'app/entities/grocery-list/grocery-list.service';
import { GroceryList } from 'app/shared/model/grocery-list.model';

describe('Component Tests', () => {
    describe('GroceryList Management Update Component', () => {
        let comp: GroceryListUpdateComponent;
        let fixture: ComponentFixture<GroceryListUpdateComponent>;
        let service: GroceryListService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [GroceryListUpdateComponent]
            })
                .overrideTemplate(GroceryListUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(GroceryListUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GroceryListService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new GroceryList(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.groceryList = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new GroceryList();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.groceryList = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
