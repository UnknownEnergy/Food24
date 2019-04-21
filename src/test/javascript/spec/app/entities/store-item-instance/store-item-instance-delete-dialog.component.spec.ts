/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Food24TestModule } from '../../../test.module';
import { StoreItemInstanceDeleteDialogComponent } from 'app/entities/store-item-instance/store-item-instance-delete-dialog.component';
import { StoreItemInstanceService } from 'app/entities/store-item-instance/store-item-instance.service';

describe('Component Tests', () => {
    describe('StoreItemInstance Management Delete Component', () => {
        let comp: StoreItemInstanceDeleteDialogComponent;
        let fixture: ComponentFixture<StoreItemInstanceDeleteDialogComponent>;
        let service: StoreItemInstanceService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [StoreItemInstanceDeleteDialogComponent]
            })
                .overrideTemplate(StoreItemInstanceDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StoreItemInstanceDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StoreItemInstanceService);
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
