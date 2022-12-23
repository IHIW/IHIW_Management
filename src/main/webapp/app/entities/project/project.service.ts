import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IProject } from 'app/shared/model/project.model';

type EntityResponseType = HttpResponse<IProject>;
type EntityArrayResponseType = HttpResponse<IProject[]>;

@Injectable({ providedIn: 'root' })
export class ProjectService {
  public resourceUrl = SERVER_API_URL + 'api/projects';

  constructor(protected http: HttpClient) {}

  create(project: IProject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(project);
    return this.http
      .post<IProject>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(project: IProject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(project);
    return this.http
      .put<IProject>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IProject>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IProject[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  getMy(): Observable<EntityArrayResponseType> {
    return this.http
      .get<IProject[]>(this.resourceUrl + '/my', { observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  subscribe(id: number): Observable<HttpResponse<any>> {
    return this.http.post<any>(`${this.resourceUrl}/${id}/subscribe`, { observe: 'response' });
  }

  unsubscribe(id: number): Observable<HttpResponse<any>> {
    return this.http.post<any>(`${this.resourceUrl}/${id}/unsubscribe`, { observe: 'response' });
  }

  removeProjectLeader(project: number, leader: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${project}/projectleader/${leader}`, { observe: 'response' });
  }

  addProjectLeader(project: number, leader: number): Observable<HttpResponse<any>> {
    return this.http.put<any>(`${this.resourceUrl}/${project}/projectleader/${leader}`, { observe: 'response' });
  }

  createProjectZip(project: IProject): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(project);
    const createProjectUrl = SERVER_API_URL + 'api/uploads/projectsummary';
    const formData: FormData = new FormData();
    formData.append('projectId', String(project.id));
    formData.append('summaryFileName', 'Project.'.concat(String(project.id), '.Downloads.zip.TEMP'));
    formData.append('summaryFileType', String('OTHER'));

    return this.http
      .put<IProject>(createProjectUrl, formData, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  protected convertDateFromClient(project: IProject): IProject {
    const copy: IProject = Object.assign({}, project, {
      createdAt: project.createdAt != null && project.createdAt.isValid() ? project.createdAt.toJSON() : null,
      modifiedAt: project.modifiedAt != null && project.modifiedAt.isValid() ? project.modifiedAt.toJSON() : null
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
      res.body.forEach((project: IProject) => {
        project.createdAt = project.createdAt != null ? moment(project.createdAt) : null;
        project.modifiedAt = project.modifiedAt != null ? moment(project.modifiedAt) : null;
      });
    }
    return res;
  }
}
