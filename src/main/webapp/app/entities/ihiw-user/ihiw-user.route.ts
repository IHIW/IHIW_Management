import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { IhiwUser } from 'app/shared/model/ihiw-user.model';
import { IhiwUserService } from './ihiw-user.service';
import { IhiwUserComponent } from './ihiw-user.component';
import { IhiwUserDetailComponent } from './ihiw-user-detail.component';
import { IhiwUserUpdateComponent } from './ihiw-user-update.component';
import { IhiwUserDeletePopupComponent } from './ihiw-user-delete-dialog.component';
import { IIhiwUser } from 'app/shared/model/ihiw-user.model';

@Injectable({ providedIn: 'root' })
export class IhiwUserResolve implements Resolve<IIhiwUser> {
  constructor(private service: IhiwUserService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IIhiwUser> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<IhiwUser>) => response.ok),
        map((ihiwUser: HttpResponse<IhiwUser>) => ihiwUser.body)
      );
    }
    return of(new IhiwUser());
  }
}

export const ihiwUserRoute: Routes = [
  {
    path: '',
    component: IhiwUserComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.ihiwUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: IhiwUserDetailComponent,
    resolve: {
      ihiwUser: IhiwUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.ihiwUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: IhiwUserUpdateComponent,
    resolve: {
      ihiwUser: IhiwUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.ihiwUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: IhiwUserUpdateComponent,
    resolve: {
      ihiwUser: IhiwUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.ihiwUser.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const ihiwUserPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: IhiwUserDeletePopupComponent,
    resolve: {
      ihiwUser: IhiwUserResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.ihiwUser.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
