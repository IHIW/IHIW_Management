import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Project } from 'app/shared/model/project.model';
import { ProjectService } from './project.service';
import { ProjectComponent } from './project.component';
import { ProjectDetailComponent } from './project-detail.component';
import { ProjectUpdateComponent } from './project-update.component';
import { ProjectDeletePopupComponent } from './project-delete-dialog.component';
import { ProjectSubscribePopupComponent } from './project-subscribe-dialog.component';
import { IProject } from 'app/shared/model/project.model';

@Injectable({ providedIn: 'root' })
export class ProjectResolve implements Resolve<IProject> {
  constructor(private service: ProjectService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IProject> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Project>) => response.ok),
        map((project: HttpResponse<Project>) => project.body)
      );
    }
    return of(new Project());
  }
}

export const projectRoute: Routes = [
  {
    path: '',
    component: ProjectComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.project.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: ProjectDetailComponent,
    resolve: {
      project: ProjectResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.project.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: ProjectUpdateComponent,
    resolve: {
      project: ProjectResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.project.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: ProjectUpdateComponent,
    resolve: {
      project: ProjectResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.project.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const projectPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: ProjectDeletePopupComponent,
    resolve: {
      project: ProjectResolve
    },
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'ihiwManagementApp.project.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: ':id/subscribe',
    component: ProjectSubscribePopupComponent,
    resolve: {
      project: ProjectResolve
    },
    data: {
      authorities: ['PI', 'ProjectLeader'],
      pageTitle: 'ihiwManagementApp.project.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
