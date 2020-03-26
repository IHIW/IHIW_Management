import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IhiwLab } from 'app/shared/model/ihiw-lab.model';
import { IhiwLabService } from './ihiw-lab.service';
import { IhiwLabComponent } from './ihiw-lab.component';
import { IhiwLabDetailComponent } from './ihiw-lab-detail.component';
import { IhiwLabUpdateComponent } from './ihiw-lab-update.component';
import { IhiwLabDeletePopupComponent } from './ihiw-lab-delete-dialog.component';
import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';
import { UserMgmtComponent } from 'app/admin';
import { JhiResolvePagingParams } from 'ng-jhipster';

@Injectable({ providedIn: 'root' })
export class IhiwLabResolve implements Resolve<IIhiwLab> {
  constructor(private service: IhiwLabService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIhiwLab> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<IhiwLab>) => response.ok),
        map((ihiwLab: HttpResponse<IhiwLab>) => ihiwLab.body)
      );
    }
    return of(new IhiwLab());
  }
}

export const ihiwLabRoute: Routes = [
  {
    path: '',
    component: IhiwLabComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      pageTitle: 'ihiwManagementApp.ihiwLab.home.title',
      page: 1
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IhiwLabDetailComponent,
    resolve: {
      ihiwLab: IhiwLabResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.ihiwLab.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IhiwLabUpdateComponent,
    resolve: {
      ihiwLab: IhiwLabResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.ihiwLab.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IhiwLabUpdateComponent,
    resolve: {
      ihiwLab: IhiwLabResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.ihiwLab.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const ihiwLabPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: IhiwLabDeletePopupComponent,
    resolve: {
      ihiwLab: IhiwLabResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.ihiwLab.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
