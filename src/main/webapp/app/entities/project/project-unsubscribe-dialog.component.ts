import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from './project.service';

@Component({
  selector: 'jhi-project-unsubscribe-dialog',
  templateUrl: './project-unsubscribe-dialog.component.html'
})
export class ProjectUnsubscribeDialogComponent {
  project: IProject;

  constructor(protected projectService: ProjectService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmUnsubscribe(id: number) {
    this.projectService.unsubscribe(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'projectListModification',
        content: 'Unsubscribed from a project'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-project-unsubscribe-popup',
  template: ''
})
export class ProjectUnsubscribePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ project }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(ProjectUnsubscribeDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.project = project;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/project', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/project', { outlets: { popup: null } }]);
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
