import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUpload, Upload } from 'app/shared/model/upload.model';
import { JhiEventManager } from 'ng-jhipster';
import { Subscription } from 'rxjs';
import { UploadService } from 'app/entities/upload/upload.service';
import { HttpResponse } from '@angular/common/http';
import { filter, map } from 'rxjs/operators';

@Component({
  selector: 'jhi-upload-detail',
  templateUrl: './upload-detail.component.html'
})
export class UploadDetailComponent implements OnInit, OnDestroy {
  upload: IUpload;
  eventSubscriber: Subscription;

  constructor(protected uploadService: UploadService, protected activatedRoute: ActivatedRoute, protected eventManager: JhiEventManager) {}

  ngOnInit() {
    this.loadAll();
    this.registerChangeInUpload();
  }

  loadAll() {
    const id = parseInt(this.activatedRoute.snapshot.paramMap.get('id'), 10);
    this.uploadService
      .find(id)
      .pipe(
        filter((response: HttpResponse<Upload>) => response.ok),
        map((upload: HttpResponse<Upload>) => upload.body)
      )
      .subscribe((upload: Upload) => {
        this.upload = upload;
        if (this.upload.validations.length < 1) {
          setTimeout(() => {
            this.eventManager.broadcast({ name: 'uploadModification', content: 'Reload' });
          }, 5000);
        }
      });
  }

  registerChangeInUpload() {
    this.eventSubscriber = this.eventManager.subscribe('uploadModification', response => this.loadAll());
  }

  sortBy(list, prop: string) {
    return list.sort((a, b) => (a[prop] > b[prop] ? 1 : a[prop] === b[prop] ? 0 : -1));
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  previousState() {
    window.history.back();
  }
}
