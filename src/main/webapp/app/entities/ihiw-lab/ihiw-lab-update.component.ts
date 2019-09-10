import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IIhiwLab, IhiwLab } from 'app/shared/model/ihiw-lab.model';
import { IhiwLabService } from './ihiw-lab.service';
import { IProject } from 'app/shared/model/project.model';
import { ProjectService } from 'app/entities/project';

@Component({
  selector: 'jhi-ihiw-lab-update',
  templateUrl: './ihiw-lab-update.component.html'
})
export class IhiwLabUpdateComponent implements OnInit {
  isSaving: boolean;

  projects: IProject[];

  editForm = this.fb.group({
    id: [],
    labCode: [null, [Validators.required]],
    title: [],
    firstName: [null, [Validators.required]],
    lastName: [null, [Validators.required]],
    director: [],
    department: [],
    institution: [],
    address1: [],
    address2: [],
    sAddress1: [],
    sAddress: [],
    city: [],
    state: [],
    zip: [],
    country: [],
    phone: [],
    fax: [],
    email: [null, [Validators.required]],
    url: [],
    oldLabCode: [],
    sName: [],
    sPhone: [],
    sEmail: [],
    dName: [],
    dEmail: [],
    dPhone: [],
    createdAt: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected ihiwLabService: IhiwLabService,
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ ihiwLab }) => {
      this.updateForm(ihiwLab);
    });
    this.projectService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IProject[]>) => mayBeOk.ok),
        map((response: HttpResponse<IProject[]>) => response.body)
      )
      .subscribe((res: IProject[]) => (this.projects = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(ihiwLab: IIhiwLab) {
    this.editForm.patchValue({
      id: ihiwLab.id,
      labCode: ihiwLab.labCode,
      title: ihiwLab.title,
      firstName: ihiwLab.firstName,
      lastName: ihiwLab.lastName,
      director: ihiwLab.director,
      department: ihiwLab.department,
      institution: ihiwLab.institution,
      address1: ihiwLab.address1,
      address2: ihiwLab.address2,
      sAddress1: ihiwLab.sAddress1,
      sAddress: ihiwLab.sAddress,
      city: ihiwLab.city,
      state: ihiwLab.state,
      zip: ihiwLab.zip,
      country: ihiwLab.country,
      phone: ihiwLab.phone,
      fax: ihiwLab.fax,
      email: ihiwLab.email,
      url: ihiwLab.url,
      oldLabCode: ihiwLab.oldLabCode,
      sName: ihiwLab.sName,
      sPhone: ihiwLab.sPhone,
      sEmail: ihiwLab.sEmail,
      dName: ihiwLab.dName,
      dEmail: ihiwLab.dEmail,
      dPhone: ihiwLab.dPhone,
      createdAt: ihiwLab.createdAt != null ? ihiwLab.createdAt.format(DATE_TIME_FORMAT) : null
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const ihiwLab = this.createFromForm();
    if (ihiwLab.id !== undefined) {
      this.subscribeToSaveResponse(this.ihiwLabService.update(ihiwLab));
    } else {
      this.subscribeToSaveResponse(this.ihiwLabService.create(ihiwLab));
    }
  }

  private createFromForm(): IIhiwLab {
    return {
      ...new IhiwLab(),
      id: this.editForm.get(['id']).value,
      labCode: this.editForm.get(['labCode']).value,
      title: this.editForm.get(['title']).value,
      firstName: this.editForm.get(['firstName']).value,
      lastName: this.editForm.get(['lastName']).value,
      director: this.editForm.get(['director']).value,
      department: this.editForm.get(['department']).value,
      institution: this.editForm.get(['institution']).value,
      address1: this.editForm.get(['address1']).value,
      address2: this.editForm.get(['address2']).value,
      sAddress1: this.editForm.get(['sAddress1']).value,
      sAddress: this.editForm.get(['sAddress']).value,
      city: this.editForm.get(['city']).value,
      state: this.editForm.get(['state']).value,
      zip: this.editForm.get(['zip']).value,
      country: this.editForm.get(['country']).value,
      phone: this.editForm.get(['phone']).value,
      fax: this.editForm.get(['fax']).value,
      email: this.editForm.get(['email']).value,
      url: this.editForm.get(['url']).value,
      oldLabCode: this.editForm.get(['oldLabCode']).value,
      sName: this.editForm.get(['sName']).value,
      sPhone: this.editForm.get(['sPhone']).value,
      sEmail: this.editForm.get(['sEmail']).value,
      dName: this.editForm.get(['dName']).value,
      dEmail: this.editForm.get(['dEmail']).value,
      dPhone: this.editForm.get(['dPhone']).value,
      createdAt:
        this.editForm.get(['createdAt']).value != null ? moment(this.editForm.get(['createdAt']).value, DATE_TIME_FORMAT) : undefined
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIhiwLab>>) {
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

  trackProjectById(index: number, item: IProject) {
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
