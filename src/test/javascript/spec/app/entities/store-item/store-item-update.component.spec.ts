/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Food24TestModule } from '../../../test.module';
import { StoreItemUpdateComponent } from 'app/entities/store-item/store-item-update.component';
import { StoreItemService } from 'app/entities/store-item/store-item.service';
import { StoreItem } from 'app/shared/model/store-item.model';

describe('Component Tests', () => {
    describe('StoreItem Management Update Component', () => {
        let comp: StoreItemUpdateComponent;
        let fixture: ComponentFixture<StoreItemUpdateComponent>;
        let service: StoreItemService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [StoreItemUpdateComponent]
            })
                .overrideTemplate(StoreItemUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StoreItemUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StoreItemService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new StoreItem(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.storeItem = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new StoreItem();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.storeItem = entity;
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
