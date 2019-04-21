/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { Food24TestModule } from '../../../test.module';
import { FamilyGroupComponent } from 'app/entities/family-group/family-group.component';
import { FamilyGroupService } from 'app/entities/family-group/family-group.service';
import { FamilyGroup } from 'app/shared/model/family-group.model';

describe('Component Tests', () => {
    describe('FamilyGroup Management Component', () => {
        let comp: FamilyGroupComponent;
        let fixture: ComponentFixture<FamilyGroupComponent>;
        let service: FamilyGroupService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [FamilyGroupComponent],
                providers: []
            })
                .overrideTemplate(FamilyGroupComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FamilyGroupComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FamilyGroupService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new FamilyGroup(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.familyGroups[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
