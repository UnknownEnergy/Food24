import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFamilyGroup } from 'app/shared/model/family-group.model';
import { FamilyGroupService } from './family-group.service';

@Component({
    selector: 'jhi-family-group-delete-dialog',
    templateUrl: './family-group-delete-dialog.component.html'
})
export class FamilyGroupDeleteDialogComponent {
    familyGroup: IFamilyGroup;

    constructor(
        protected familyGroupService: FamilyGroupService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.familyGroupService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'familyGroupListModification',
                content: 'Deleted an familyGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-family-group-delete-popup',
    template: ''
})
export class FamilyGroupDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ familyGroup }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FamilyGroupDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.familyGroup = familyGroup;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/family-group', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/family-group', { outlets: { popup: null } }]);
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
