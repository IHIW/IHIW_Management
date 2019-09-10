import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Upload } from 'app/shared/model/upload.model';
import { UploadService } from './upload.service';
import { UploadComponent } from './upload.component';
import { UploadDetailComponent } from './upload-detail.component';
import { UploadUpdateComponent } from './upload-update.component';
import { UploadDeletePopupComponent } from './upload-delete-dialog.component';
import { IUpload } from 'app/shared/model/upload.model';

@Injectable({ providedIn: 'root' })
export class UploadResolve implements Resolve<IUpload> {
  constructor(private service: UploadService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IUpload> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Upload>) => response.ok),
        map((upload: HttpResponse<Upload>) => upload.body)
      );
    }
    return of(new Upload());
  }
}

export const uploadRoute: Routes = [
  {
    path: '',
    component: UploadComponent,
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.upload.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: UploadDetailComponent,
    resolve: {
      upload: UploadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.upload.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: UploadUpdateComponent,
    resolve: {
      upload: UploadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.upload.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: UploadUpdateComponent,
    resolve: {
      upload: UploadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.upload.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const uploadPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: UploadDeletePopupComponent,
    resolve: {
      upload: UploadResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'ihiwManagementApp.upload.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
