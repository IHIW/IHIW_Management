import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService, JhiParseLinks } from 'ng-jhipster';

import { IProject } from 'app/shared/model/project.model';
import { AccountService, UserService, User } from 'app/core';
import { ProjectService } from './project.service';
import { IhiwUserService } from '../ihiw-user/ihiw-user.service';
import { IIhiwUser } from '../../shared/model/ihiw-user.model';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';

@Component({
  selector: 'jhi-project',
  templateUrl: './project.component.html'
})
export class ProjectComponent implements OnInit, OnDestroy {
  projects: IProject[];
  currentAccount: any;
  predicate: any;
  routeData: any;
  page: any;
  previousPage: any;
  itemsPerPage: any;
  reverse: any;
  totalItems: any;
  eventSubscriber: Subscription;
  ihiwUser: IIhiwUser;

  constructor(
    protected projectService: ProjectService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService,
    protected ihiwUserService: IhiwUserService,
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
    this.projectService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        sort: this.sort()
      })
      .subscribe(
        (res: HttpResponse<IProject[]>) => this.onSuccess(res.body, res.headers),
        (res: HttpResponse<any>) => this.onError(res.body)
      );
    this.ihiwUserService;
    //   //.my()
    //   .query({
    //     page: this.page - 1,
    //     size: this.itemsPerPage,
    //     sort: this.sort()
    //   })
    //   .subscribe(
    //     (res: HttpResponse<IIhiwUser[]>) => this.onSuccess(res.body, res.headers),
    //     (res: HttpResponse<any>) => this.onError(res.body)
    //   );
  }

  ngOnInit() {
    this.loadAll();
    this.accountService.identity().then(account => {
      this.currentAccount = account;
    });
    this.registerChangeInProjects();
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.eventSubscriber);
  }

  trackId(index: number, item: IProject) {
    return item.id;
  }

  registerChangeInProjects() {
    this.eventSubscriber = this.eventManager.subscribe('projectListModification', response => this.loadAll());
  }

  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  setActive(project, isActivated) {
    project.activated = isActivated;

    this.projectService.update(project).subscribe(response => {
      if (response.status === 200) {
        this.loadAll();
      } else {
      }
    });
  }

  sort() {
    const result = [this.predicate + ',' + (this.reverse ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  transition() {
    this.router.navigate(['/project'], {
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
    this.projects = data;
  }

  myProjectStatus(project: IProject, status: string) {
    for (const lab of project.labs) {
      if (this.ihiwUser !== undefined && lab.lab.id === this.ihiwUser.lab.id && lab.status === status) {
        return true;
      }
    }
    return false;
  }
}
