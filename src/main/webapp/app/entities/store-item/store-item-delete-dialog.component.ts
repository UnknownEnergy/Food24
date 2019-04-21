import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IStoreItem } from 'app/shared/model/store-item.model';
import { StoreItemService } from './store-item.service';

@Component({
    selector: 'jhi-store-item-delete-dialog',
    templateUrl: './store-item-delete-dialog.component.html'
})
export class StoreItemDeleteDialogComponent {
    storeItem: IStoreItem;

    constructor(
        protected storeItemService: StoreItemService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.storeItemService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'storeItemListModification',
                content: 'Deleted an storeItem'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-store-item-delete-popup',
    template: ''
})
export class StoreItemDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ storeItem }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(StoreItemDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.storeItem = storeItem;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/store-item', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/store-item', { outlets: { popup: null } }]);
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
