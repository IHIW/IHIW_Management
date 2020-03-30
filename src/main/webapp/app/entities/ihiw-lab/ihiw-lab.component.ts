import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';

import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';
import { AccountService, UserService, User } from 'app/core';
import { IhiwLabService } from './ihiw-lab.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-ihiw-lab',
  templateUrl: './ihiw-lab.component.html'
})
export class IhiwLabComponent implements OnInit, OnDestroy {
  ihiwLabs: IIhiwLab[];
  currentAccount: any;
  predicate: any;
  routeData: any;
  page: any;
  previousPage: any;
  itemsPerPage: any;
  reverse: any;
  totalItems: any;
  eventSubscriber: Subscription;

  constructor(
    protected ihiwLabService: IhiwLabService,
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
      this.reverse = data['pagingParams'].ascending;
      this.predicate = data['pagingParams'].predicate;
    });
  }

  loadAll() {
    this.ihiwLabService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IIhiwLab[]>) => this.onSuccess(res.body, res.headers),
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInIhiwLabs();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IIhiwLab) {
    return item.id;
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  transition() {
    this.router.navigate(['/ihiw-lab'], {
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

  registerChangeInIhiwLabs() {
    this.eventSubscriber = this.eventManager.subscribe('ihiwLabListModification', response => this.loadAll());
  }

  private onSuccess(data, headers) {
    this.totalItems = headers.get('X-Total-Count');
    this.ihiwLabs = data;
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
