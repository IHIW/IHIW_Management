import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';

import { IUpload } from 'app/shared/model/upload.model';
import { AccountService } from 'app/core';
import { UploadService } from './upload.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IProject } from 'app/shared/model/project.model';

@Component({
  selector: 'jhi-upload',
  templateUrl: './upload.component.html'
})
export class UploadComponent implements OnInit, OnDestroy {
  uploads: IUpload[];
  currentAccount: any;
  predicate: any;
  routeData: any;
  page: any;
  previousPage: any;
  itemsPerPage: any;
  reverse: any;
  totalItems: any;
  eventSubscriber: Subscription;
  refreshing: boolean;

  constructor(
    protected uploadService: UploadService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private parseLinks: JhiParseLinks,
    private modalService: NgbModal
  ) {
    this.itemsPerPage = 500;
    this.routeData = this.activatedRoute.data.subscribe(data => {
      this.page = data['pagingParams'].page;
      this.previousPage = data['pagingParams'].page;
      this.reverse = false;
      this.predicate = 'id';
    });
    this.refreshing = false;
  }

  loadAll() {
    this.uploadService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IUpload[]>) => {
          this.onSuccess(res.body, res.headers);
          let validationActive = false;
          for (const upload of this.uploads) {
            upload.valid = null;
            for (const validation of upload.validations) {
              if (upload.valid == null) {
                upload.valid = validation.valid;
              } else {
                upload.valid = upload.valid && validation.valid;
              }
            }
            if (upload.validations.length < 1) {
              validationActive = true;
            }
          }
          if (validationActive && !this.refreshing) {
            this.refreshing = true;
            setTimeout(() => {
              this.eventManager.broadcast({ name: 'uploadListModification', content: 'Reload' });
            }, 5000);
          }
        },
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInUploads();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IUpload) {
    return item.id;
  }

  registerChangeInUploads() {
    this.eventSubscriber = this.eventManager.subscribe('uploadListModification', response => {
      this.refreshing = false;
      this.loadAll();
    });
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id,desc');
    }
    return result;
  }

  transition() {
    this.router.navigate(['/upload'], {
      queryParams: {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.predicate + ',' + (this.reverse ? 'asc' : 'desc')
      }
    });
    this.loadAll();
  }

  loadPage(page: number) {
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.transition();
    }
  }

  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.uploads = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
