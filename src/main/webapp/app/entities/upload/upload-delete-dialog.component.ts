import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IUpload } from 'app/shared/model/upload.model';
import { UploadService } from './upload.service';

@Component({
  selector: 'jhi-upload-delete-dialog',
  templateUrl: './upload-delete-dialog.component.html'
})
export class UploadDeleteDialogComponent {
  upload: IUpload;

  constructor(protected uploadService: UploadService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.uploadService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'uploadListModification',
        content: 'Deleted an upload'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-upload-delete-popup',
  template: ''
})
export class UploadDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ upload }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(UploadDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.upload = upload;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/upload', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/upload', { outlets: { popup: null } }]);
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
