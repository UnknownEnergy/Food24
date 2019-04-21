/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Food24TestModule } from '../../../test.module';
import { StoreItemInstanceUpdateComponent } from 'app/entities/store-item-instance/store-item-instance-update.component';
import { StoreItemInstanceService } from 'app/entities/store-item-instance/store-item-instance.service';
import { StoreItemInstance } from 'app/shared/model/store-item-instance.model';

describe('Component Tests', () => {
    describe('StoreItemInstance Management Update Component', () => {
        let comp: StoreItemInstanceUpdateComponent;
        let fixture: ComponentFixture<StoreItemInstanceUpdateComponent>;
        let service: StoreItemInstanceService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [StoreItemInstanceUpdateComponent]
            })
                .overrideTemplate(StoreItemInstanceUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(StoreItemInstanceUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StoreItemInstanceService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new StoreItemInstance(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.storeItemInstance = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new StoreItemInstance();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.storeItemInstance = entity;
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
