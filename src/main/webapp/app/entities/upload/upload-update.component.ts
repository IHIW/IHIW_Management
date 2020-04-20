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
import { IhiwUserService } from 'app/entities/ihiw-user';

@Component({
  selector: 'jhi-upload-update',
  templateUrl: './upload-update.component.html'
})
export class UploadUpdateComponent implements OnInit {
  isSaving: boolean;
  file: File;

  editForm = this.fb.group({
    id: [],
    type: [],
    createdAt: [],
    modifiedAt: [],
    fileName: [],
    valid: [],
    validationFeedback: [],
    enabled: [],
    createdBy: []
  });

  constructor(
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
  }

  updateForm(upload: IUpload) {
    this.editForm.patchValue({
      id: upload.id,
      type: upload.type,
      createdAt: upload.createdAt != null ? upload.createdAt.format(DATE_TIME_FORMAT) : null,
      modifiedAt: upload.modifiedAt != null ? upload.modifiedAt.format(DATE_TIME_FORMAT) : null,
      fileName: upload.fileName,
      valid: upload.valid,
      validationFeedback: upload.validationFeedback,
      enabled: upload.enabled,
      createdBy: upload.createdBy
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
      this.subscribeToSaveResponse(this.uploadService.update(upload));
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
      valid: this.editForm.get(['valid']).value,
      enabled: this.editForm.get(['enabled']).value,
      createdBy: this.editForm.get(['createdBy']).value
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

  trackIhiwUserById(index: number, item: IIhiwUser) {
    return item.id;
  }
}
