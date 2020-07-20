import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IUpload, Upload } from 'app/shared/model/upload.model';
import { UploadService } from './upload.service';
import { IIhiwUser } from 'app/shared/model/ihiw-user.model';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
  selector: 'jhi-upload-update',
  templateUrl: './upload-update.component.html'
})
export class UploadUpdateComponent implements OnInit {
  isSaving: boolean;
  file: File;

  projects: IProject[];

  editForm = this.fb.group({
    id: [],
    type: [],
    createdAt: [],
    modifiedAt: [],
    fileName: [],
    enabled: [],
    createdBy: [],
    project: [null, [Validators.required]]
  });

  constructor(
    protected projectService: ProjectService,
    protected jhiAlertService: JhiAlertService,
    protected uploadService: UploadService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ upload }) => {
      this.updateForm(upload);
    });
    this.projectService
      .getMy()
      .subscribe((res: HttpResponse<IProject[]>) => (this.projects = res.body), (res: HttpResponse<any>) => this.onError(res.body));
  }

  updateForm(upload: IUpload) {
    this.editForm.patchValue({
      id: upload.id,
      type: upload.type,
      createdAt: upload.createdAt != null ? upload.createdAt.format(DATE_TIME_FORMAT) : null,
      modifiedAt: upload.modifiedAt != null ? upload.modifiedAt.format(DATE_TIME_FORMAT) : null,
      fileName: upload.fileName,
      enabled: upload.enabled,
      createdBy: upload.createdBy,
      project: upload.project
    });
  }

  previousState() {
    window.history.back();
  }

  onFileChange(event) {
    if (event.target.files.length > 0) {
      this.file = event.target.files[0];
    }
  }

  save() {
    this.isSaving = true;
    const upload = this.createFromForm();
    if (upload.id !== undefined) {
      this.subscribeToSaveResponse(this.uploadService.update(upload, this.file));
    } else {
      this.subscribeToSaveResponse(this.uploadService.createWithFile(upload, this.file));
    }
  }

  private createFromForm(): IUpload {
    return {
      ...new Upload(),
      id: this.editForm.get(['id']).value,
      type: this.editForm.get(['type']).value,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined,
      modifiedAt:
        this.editForm.get(['modifiedAt']).value != null ? moment(this.editForm.get(['modifiedAt']).value, DATE_TIME_FORMAT) : undefined,
      fileName: this.editForm.get(['fileName']).value,
      enabled: this.editForm.get(['enabled']).value,
      createdBy: this.editForm.get(['createdBy']).value,
      project: this.editForm.get(['project']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUpload>>) {
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

  setProject(event: any) {
    this.editForm.get(['project']).setValue(this.projects[event.target.selectedIndex - 1]);
  }

  trackIhiwUserById(index: number, item: IIhiwUser) {
    return item.id;
  }
}
