import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';

type EntityResponseType = HttpResponse<IIhiwLab>;
type EntityArrayResponseType = HttpResponse<IIhiwLab[]>;

@Injectable({ providedIn: 'root' })
export class IhiwLabService {
  public resourceUrl = SERVER_API_URL + 'api/ihiw-labs';

  constructor(protected http: HttpClient) {}

  create(ihiwLab: IIhiwLab): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ihiwLab);
    return this.http
      .post<IIhiwLab>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(ihiwLab: IIhiwLab): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ihiwLab);
    return this.http
      .put<IIhiwLab>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIhiwLab>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIhiwLab[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(ihiwLab: IIhiwLab): IIhiwLab {
    const copy: IIhiwLab = Object.assign({}, ihiwLab, {
      createdAt: ihiwLab.createdAt != null && ihiwLab.createdAt.isValid() ? ihiwLab.createdAt.toJSON() : null
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.createdAt = res.body.createdAt != null ? moment(res.body.createdAt) : null;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((ihiwLab: IIhiwLab) => {
        ihiwLab.createdAt = ihiwLab.createdAt != null ? moment(ihiwLab.createdAt) : null;
      });
    }
    return res;
  }
}
