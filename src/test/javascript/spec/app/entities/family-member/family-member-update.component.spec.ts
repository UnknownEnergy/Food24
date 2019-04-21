/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { Food24TestModule } from '../../../test.module';
import { FamilyMemberUpdateComponent } from 'app/entities/family-member/family-member-update.component';
import { FamilyMemberService } from 'app/entities/family-member/family-member.service';
import { FamilyMember } from 'app/shared/model/family-member.model';

describe('Component Tests', () => {
    describe('FamilyMember Management Update Component', () => {
        let comp: FamilyMemberUpdateComponent;
        let fixture: ComponentFixture<FamilyMemberUpdateComponent>;
        let service: FamilyMemberService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [FamilyMemberUpdateComponent]
            })
                .overrideTemplate(FamilyMemberUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FamilyMemberUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FamilyMemberService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new FamilyMember(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.familyMember = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new FamilyMember();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.familyMember = entity;
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
