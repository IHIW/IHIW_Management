import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIhiwUser } from 'app/shared/model/ihiw-user.model';
import { IhiwUserService } from './ihiw-user.service';

@Component({
  selector: 'jhi-ihiw-user-delete-dialog',
  templateUrl: './ihiw-user-delete-dialog.component.html'
})
export class IhiwUserDeleteDialogComponent {
  ihiwUser: IIhiwUser;

  constructor(protected ihiwUserService: IhiwUserService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.ihiwUserService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'ihiwUserListModification',
        content: 'Deleted an ihiwUser'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-ihiw-user-delete-popup',
  template: ''
})
export class IhiwUserDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ihiwUser }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(IhiwUserDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.ihiwUser = ihiwUser;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/ihiw-user', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/ihiw-user', { outlets: { popup: null } }]);
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
