/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Food24TestModule } from '../../../test.module';
import { FamilyMemberDetailComponent } from 'app/entities/family-member/family-member-detail.component';
import { FamilyMember } from 'app/shared/model/family-member.model';

describe('Component Tests', () => {
    describe('FamilyMember Management Detail Component', () => {
        let comp: FamilyMemberDetailComponent;
        let fixture: ComponentFixture<FamilyMemberDetailComponent>;
        const route = ({ data: of({ familyMember: new FamilyMember(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [FamilyMemberDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FamilyMemberDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FamilyMemberDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.familyMember).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
