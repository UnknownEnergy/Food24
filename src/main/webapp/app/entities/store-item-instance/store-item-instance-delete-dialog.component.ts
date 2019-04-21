import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStoreItemInstance } from 'app/shared/model/store-item-instance.model';
import { StoreItemInstanceService } from './store-item-instance.service';

@Component({
    selector: 'jhi-store-item-instance-delete-dialog',
    templateUrl: './store-item-instance-delete-dialog.component.html'
})
export class StoreItemInstanceDeleteDialogComponent {
    storeItemInstance: IStoreItemInstance;

    constructor(
        protected storeItemInstanceService: StoreItemInstanceService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.storeItemInstanceService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'storeItemInstanceListModification',
                content: 'Deleted an storeItemInstance'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-store-item-instance-delete-popup',
    template: ''
})
export class StoreItemInstanceDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ storeItemInstance }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(StoreItemInstanceDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.storeItemInstance = storeItemInstance;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/store-item-instance', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/store-item-instance', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
