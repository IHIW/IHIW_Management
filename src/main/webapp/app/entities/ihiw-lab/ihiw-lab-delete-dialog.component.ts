import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';
import { IhiwLabService } from './ihiw-lab.service';

@Component({
  selector: 'jhi-ihiw-lab-delete-dialog',
  templateUrl: './ihiw-lab-delete-dialog.component.html'
})
export class IhiwLabDeleteDialogComponent {
  ihiwLab: IIhiwLab;

  constructor(protected ihiwLabService: IhiwLabService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.ihiwLabService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'ihiwLabListModification',
        content: 'Deleted an ihiwLab'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-ihiw-lab-delete-popup',
  template: ''
})
export class IhiwLabDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ihiwLab }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(IhiwLabDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.ihiwLab = ihiwLab;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/ihiw-lab', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/ihiw-lab', { outlets: { popup: null } }]);
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
