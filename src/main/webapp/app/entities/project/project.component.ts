import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { IProject, ProjectComponentEnum } from 'app/shared/model/project.model';
import { AccountService } from 'app/core';
import { ProjectService } from './project.service';
import { IhiwUserService } from '../ihiw-user/ihiw-user.service';
import { IIhiwUser } from '../../shared/model/ihiw-user.model';

@Component({
  selector: 'jhi-project',
  templateUrl: './project.component.html'
})
export class ProjectComponent implements OnInit, OnDestroy {
  projects: IProject[];
  // @ts-ignore It needs the any ProjectComponentEnum, without it reverse mapping does not work
  ProjectComponentEnum: any = ProjectComponentEnum;
  currentAccount: any;
  eventSubscriber: Subscription;
  ihiwUser: IIhiwUser;

  constructor(
    protected projectService: ProjectService,
    protected jhiAlertService: JhiAlertService,
    protected eventManager: JhiEventManager,
    protected accountService: AccountService,
    protected ihiwUserService: IhiwUserService
  ) {}

  loadAll() {
    this.projectService
      .query()
      .pipe(
        filter((res: HttpResponse<IProject[]>) => res.ok),
        map((res: HttpResponse<IProject[]>) => res.body)
      )
      .subscribe(
        (res: IProject[]) => {
          this.projects = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
    this.ihiwUserService
      .my()
      .pipe(
        filter((res: HttpResponse<IIhiwUser>) => res.ok),
        map((res: HttpResponse<IIhiwUser>) => res.body)
      )
      .subscribe(
        (res: IIhiwUser) => {
          this.ihiwUser = res;
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
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

  myProjectStatus(project: IProject, status: string) {
    for (const lab of project.labs) {
      if (lab.lab.id === this.ihiwUser.lab.id && lab.status === status) {
        return true;
      }
    }
    return false;
  }
}
