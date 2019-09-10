import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';
import { AccountService } from 'app/core';
import { IhiwLabService } from './ihiw-lab.service';

@Component({
  selector: 'jhi-ihiw-lab',
  templateUrl: './ihiw-lab.component.html'
})
export class IhiwLabComponent implements OnInit, OnDestroy {
  ihiwLabs: IIhiwLab[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected ihiwLabService: IhiwLabService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.ihiwLabService
      .query()
      .pipe(
        filter((res: HttpResponse<IIhiwLab[]>) => res.ok),
        map((res: HttpResponse<IIhiwLab[]>) => res.body)
      )
      .subscribe(
        (res: IIhiwLab[]) => {
          this.ihiwLabs = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
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

  registerChangeInIhiwLabs() {
    this.eventSubscriber = this.eventManager.subscribe('ihiwLabListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
