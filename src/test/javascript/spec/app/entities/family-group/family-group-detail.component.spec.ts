/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { Food24TestModule } from '../../../test.module';
import { FamilyGroupDetailComponent } from 'app/entities/family-group/family-group-detail.component';
import { FamilyGroup } from 'app/shared/model/family-group.model';

describe('Component Tests', () => {
    describe('FamilyGroup Management Detail Component', () => {
        let comp: FamilyGroupDetailComponent;
        let fixture: ComponentFixture<FamilyGroupDetailComponent>;
        const route = ({ data: of({ familyGroup: new FamilyGroup(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [FamilyGroupDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FamilyGroupDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FamilyGroupDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.familyGroup).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
