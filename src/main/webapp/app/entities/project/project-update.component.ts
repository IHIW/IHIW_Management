import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IProject, Project } from 'app/shared/model/project.model';
import { ProjectService } from './project.service';
import { IIhiwUser } from 'app/shared/model/ihiw-user.model';
import { IhiwUserService } from 'app/entities/ihiw-user';
import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';
import { IhiwLabService } from 'app/entities/ihiw-lab';

@Component({
  selector: 'jhi-project-update',
  templateUrl: './project-update.component.html'
})
export class ProjectUpdateComponent implements OnInit {
  isSaving: boolean;

  ihiwusers: IIhiwUser[];

  ihiwlabs: IIhiwLab[];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    createdAt: [],
    modifiedAt: [],
    createdBy: [],
    modifiedBy: [],
    labs: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected projectService: ProjectService,
    protected ihiwUserService: IhiwUserService,
    protected ihiwLabService: IhiwLabService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ project }) => {
      this.updateForm(project);
    });
    this.ihiwUserService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIhiwUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIhiwUser[]>) => response.body)
      )
      .subscribe((res: IIhiwUser[]) => (this.ihiwusers = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.ihiwLabService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIhiwLab[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIhiwLab[]>) => response.body)
      )
      .subscribe((res: IIhiwLab[]) => (this.ihiwlabs = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(project: IProject) {
    this.editForm.patchValue({
      id: project.id,
      name: project.name,
      description: project.description,
      createdAt: project.createdAt != null ? project.createdAt.format(DATE_TIME_FORMAT) : null,
      modifiedAt: project.modifiedAt != null ? project.modifiedAt.format(DATE_TIME_FORMAT) : null,
      createdBy: project.createdBy,
      modifiedBy: project.modifiedBy,
      labs: project.labs
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const project = this.createFromForm();
    if (project.id !== undefined) {
      this.subscribeToSaveResponse(this.projectService.update(project));
    } else {
      this.subscribeToSaveResponse(this.projectService.create(project));
    }
  }

  private createFromForm(): IProject {
    return {
      ...new Project(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      description: this.editForm.get(['description']).value,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      modifiedAt:
        this.editForm.get(['modifiedAt']).value != null ? moment(this.editForm.get(['modifiedAt']).value, DATE_TIME_FORMAT) : undefined,
      createdBy: this.editForm.get(['createdBy']).value,
      modifiedBy: this.editForm.get(['modifiedBy']).value,
      labs: this.editForm.get(['labs']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackIhiwUserById(index: number, item: IIhiwUser) {
    return item.id;
  }

  trackIhiwLabById(index: number, item: IIhiwLab) {
    return item.id;
  }

  getSelected(selectedVals: Array<any>, option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
