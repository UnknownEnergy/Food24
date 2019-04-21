/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Food24TestModule } from '../../../test.module';
import { FamilyGroupUpdateComponent } from 'app/entities/family-group/family-group-update.component';
import { FamilyGroupService } from 'app/entities/family-group/family-group.service';
import { FamilyGroup } from 'app/shared/model/family-group.model';

describe('Component Tests', () => {
    describe('FamilyGroup Management Update Component', () => {
        let comp: FamilyGroupUpdateComponent;
        let fixture: ComponentFixture<FamilyGroupUpdateComponent>;
        let service: FamilyGroupService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [FamilyGroupUpdateComponent]
            })
                .overrideTemplate(FamilyGroupUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FamilyGroupUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FamilyGroupService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new FamilyGroup(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.familyGroup = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new FamilyGroup();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.familyGroup = entity;
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
