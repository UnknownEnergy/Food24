import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFamilyMember } from 'app/shared/model/family-member.model';
import { FamilyMemberService } from './family-member.service';

@Component({
    selector: 'jhi-family-member-delete-dialog',
    templateUrl: './family-member-delete-dialog.component.html'
})
export class FamilyMemberDeleteDialogComponent {
    familyMember: IFamilyMember;

    constructor(
        protected familyMemberService: FamilyMemberService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.familyMemberService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'familyMemberListModification',
                content: 'Deleted an familyMember'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-family-member-delete-popup',
    template: ''
})
export class FamilyMemberDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ familyMember }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FamilyMemberDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.familyMember = familyMember;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/family-member', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/family-member', { outlets: { popup: null } }]);
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
