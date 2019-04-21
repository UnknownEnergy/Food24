/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { Food24TestModule } from '../../../test.module';
import { GroceryListDeleteDialogComponent } from 'app/entities/grocery-list/grocery-list-delete-dialog.component';
import { GroceryListService } from 'app/entities/grocery-list/grocery-list.service';

describe('Component Tests', () => {
    describe('GroceryList Management Delete Component', () => {
        let comp: GroceryListDeleteDialogComponent;
        let fixture: ComponentFixture<GroceryListDeleteDialogComponent>;
        let service: GroceryListService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [Food24TestModule],
                declarations: [GroceryListDeleteDialogComponent]
            })
                .overrideTemplate(GroceryListDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(GroceryListDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(GroceryListService);
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
