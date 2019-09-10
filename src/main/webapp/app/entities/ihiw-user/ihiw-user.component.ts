import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IIhiwUser } from 'app/shared/model/ihiw-user.model';
import { AccountService } from 'app/core';
import { IhiwUserService } from './ihiw-user.service';

@Component({
  selector: 'jhi-ihiw-user',
  templateUrl: './ihiw-user.component.html'
})
export class IhiwUserComponent implements OnInit, OnDestroy {
  ihiwUsers: IIhiwUser[];
  currentAccount: any;
  eventSubscriber: Subscription;

  constructor(
    protected ihiwUserService: IhiwUserService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService
  ) {}

  loadAll() {
    this.ihiwUserService
      .query()
      .pipe(
        filter((res: HttpResponse<IIhiwUser[]>) => res.ok),
        map((res: HttpResponse<IIhiwUser[]>) => res.body)
      )
      .subscribe(
        (res: IIhiwUser[]) => {
          this.ihiwUsers = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInIhiwUsers();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IIhiwUser) {
    return item.id;
  }

  registerChangeInIhiwUsers() {
    this.eventSubscriber = this.eventManager.subscribe('ihiwUserListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
}
