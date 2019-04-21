/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Food24TestModule } from '../../../test.module';
import { FamilyMemberComponent } from 'app/entities/family-member/family-member.component';
import { FamilyMemberService } from 'app/entities/family-member/family-member.service';
import { FamilyMember } from 'app/shared/model/family-member.model';

describe('Component Tests', () => {
    describe('FamilyMember Management Component', () => {
        let comp: FamilyMemberComponent;
        let fixture: ComponentFixture<FamilyMemberComponent>;
        let service: FamilyMemberService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [FamilyMemberComponent],
                providers: []
            })
                .overrideTemplate(FamilyMemberComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FamilyMemberComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FamilyMemberService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new FamilyMember(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.familyMembers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
