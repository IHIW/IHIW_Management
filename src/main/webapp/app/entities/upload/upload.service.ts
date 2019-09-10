import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUpload } from 'app/shared/model/upload.model';

type EntityResponseType = HttpResponse<IUpload>;
type EntityArrayResponseType = HttpResponse<IUpload[]>;

@Injectable({ providedIn: 'root' })
export class UploadService {
  public resourceUrl = SERVER_API_URL + 'api/uploads';

  constructor(protected http: HttpClient) {}

  create(upload: IUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(upload);
    return this.http
      .post<IUpload>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  createWithFile(upload: IUpload, file: File): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(upload);

    const uploadMultipartFormParam = 'upload';
    const fileMultipartFormParam = 'file';
    const formData: FormData = new FormData();
    const uploadAsJsonBlob: Blob = new Blob([JSON.stringify(copy)], { type: 'application/json' });

    formData.append(uploadMultipartFormParam, uploadAsJsonBlob);
    if (file !== undefined) {
      formData.append(fileMultipartFormParam, file);
    }

    return this.http
      .post<IUpload>(this.resourceUrl, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(upload: IUpload): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(upload);
    return this.http
      .put<IUpload>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IUpload>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IUpload[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(upload: IUpload): IUpload {
    const copy: IUpload = Object.assign({}, upload, {
      createdAt: upload.createdAt != null && upload.createdAt.isValid() ? upload.createdAt.toJSON() : null,
      modifiedAt: upload.modifiedAt != null && upload.modifiedAt.isValid() ? upload.modifiedAt.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
      res.body.modifiedAt = res.body.modifiedAt != null ? moment(res.body.modifiedAt) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((upload: IUpload) => {
        upload.createdAt = upload.createdAt != null ? moment(upload.createdAt) : null;
        upload.modifiedAt = upload.modifiedAt != null ? moment(upload.modifiedAt) : null;
      });
    }
    return res;
  }
}
