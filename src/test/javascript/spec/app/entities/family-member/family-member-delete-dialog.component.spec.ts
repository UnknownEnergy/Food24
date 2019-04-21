/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Food24TestModule } from '../../../test.module';
import { FamilyMemberDeleteDialogComponent } from 'app/entities/family-member/family-member-delete-dialog.component';
import { FamilyMemberService } from 'app/entities/family-member/family-member.service';

describe('Component Tests', () => {
    describe('FamilyMember Management Delete Component', () => {
        let comp: FamilyMemberDeleteDialogComponent;
        let fixture: ComponentFixture<FamilyMemberDeleteDialogComponent>;
        let service: FamilyMemberService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [FamilyMemberDeleteDialogComponent]
            })
                .overrideTemplate(FamilyMemberDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FamilyMemberDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FamilyMemberService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
