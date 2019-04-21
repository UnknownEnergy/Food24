/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Food24TestModule } from '../../../test.module';
import { FamilyGroupDeleteDialogComponent } from 'app/entities/family-group/family-group-delete-dialog.component';
import { FamilyGroupService } from 'app/entities/family-group/family-group.service';

describe('Component Tests', () => {
    describe('FamilyGroup Management Delete Component', () => {
        let comp: FamilyGroupDeleteDialogComponent;
        let fixture: ComponentFixture<FamilyGroupDeleteDialogComponent>;
        let service: FamilyGroupService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [FamilyGroupDeleteDialogComponent]
            })
                .overrideTemplate(FamilyGroupDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FamilyGroupDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FamilyGroupService);
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
