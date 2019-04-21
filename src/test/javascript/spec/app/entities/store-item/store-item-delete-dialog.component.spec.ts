/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Food24TestModule } from '../../../test.module';
import { StoreItemDeleteDialogComponent } from 'app/entities/store-item/store-item-delete-dialog.component';
import { StoreItemService } from 'app/entities/store-item/store-item.service';

describe('Component Tests', () => {
    describe('StoreItem Management Delete Component', () => {
        let comp: StoreItemDeleteDialogComponent;
        let fixture: ComponentFixture<StoreItemDeleteDialogComponent>;
        let service: StoreItemService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [StoreItemDeleteDialogComponent]
            })
                .overrideTemplate(StoreItemDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(StoreItemDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(StoreItemService);
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
