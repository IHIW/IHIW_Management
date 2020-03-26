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
  // eventSubscriber: Subscription;

  constructor(
    protected ihiwLabService: IhiwLabService,
    protected jhiAlertService: JhiAlertService,
    private eventManager: JhiEventManager,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private parseLinks: JhiParseLinks,
    protected accountService: AccountService,
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
      .query({ sort: this.sort() })
      .pipe(
        filter((res: HttpResponse<IIhiwLab[]>) => res.ok),
        map((res: HttpResponse<IIhiwLab[]>) => res.body)
      )
      .subscribe(
        (res: IIhiwLab[]) => {
          this.ihiwLabs = res;
        },
        (res: HttpResponse<any>) => this.onError(res.body)
      );
  }

  ngOnInit() {
    this.accountService.identity().then(account => {
      this.currentAccount = account;
      this.loadAll();
      this.registerChangeInIhiwLabs();
    });
  }

  ngOnDestroy() {
    // this.eventManager.destroy(this.eventSubscriber);
    this.routeData.unsubscribe();
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
    this.router.navigate(['/admin/ihiw-lab'], {
      queryParams: {
        page: this.page,
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
    // this.eventManager.subscribe('userListModification', response => this.loadAll());
    this.eventManager.subscribe('ihiwLabListModification', response => this.loadAll());
    // this.eventManager = this.eventManager.subscribe('ihiwLabListModification', response => this.loadAll());
  }

  protected onError(error) {
    this.jhiAlertService.error(error, null, null);
  }
}
