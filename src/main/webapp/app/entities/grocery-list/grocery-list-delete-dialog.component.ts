import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IGroceryList } from 'app/shared/model/grocery-list.model';
import { GroceryListService } from './grocery-list.service';

@Component({
    selector: 'jhi-grocery-list-delete-dialog',
    templateUrl: './grocery-list-delete-dialog.component.html'
})
export class GroceryListDeleteDialogComponent {
    groceryList: IGroceryList;

    constructor(
        protected groceryListService: GroceryListService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.groceryListService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'groceryListListModification',
                content: 'Deleted an groceryList'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-grocery-list-delete-popup',
    template: ''
})
export class GroceryListDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ groceryList }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(GroceryListDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.groceryList = groceryList;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/grocery-list', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/grocery-list', { outlets: { popup: null } }]);
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
