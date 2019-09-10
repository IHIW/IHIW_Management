import { Component, OnInit } from '@angular/core';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IIhiwUser, IhiwUser } from 'app/shared/model/ihiw-user.model';
import { IhiwUserService } from './ihiw-user.service';
import { IUser, UserService } from 'app/core';
import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';
import { IhiwLabService } from 'app/entities/ihiw-lab';

@Component({
  selector: 'jhi-ihiw-user-update',
  templateUrl: './ihiw-user-update.component.html'
})
export class IhiwUserUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  ihiwlabs: IIhiwLab[];

  editForm = this.fb.group({
    id: [],
    phone: [],
    user: [],
    lab: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected ihiwUserService: IhiwUserService,
    protected userService: UserService,
    protected ihiwLabService: IhiwLabService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ ihiwUser }) => {
      this.updateForm(ihiwUser);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.ihiwLabService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IIhiwLab[]>) => mayBeOk.ok),
        map((response: HttpResponse<IIhiwLab[]>) => response.body)
      )
      .subscribe((res: IIhiwLab[]) => (this.ihiwlabs = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(ihiwUser: IIhiwUser) {
    this.editForm.patchValue({
      id: ihiwUser.id,
      phone: ihiwUser.phone,
      user: ihiwUser.user,
      lab: ihiwUser.lab
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const ihiwUser = this.createFromForm();
    if (ihiwUser.id !== undefined) {
      this.subscribeToSaveResponse(this.ihiwUserService.update(ihiwUser));
    } else {
      this.subscribeToSaveResponse(this.ihiwUserService.create(ihiwUser));
    }
  }

  private createFromForm(): IIhiwUser {
    return {
      ...new IhiwUser(),
      id: this.editForm.get(['id']).value,
      phone: this.editForm.get(['phone']).value,
      user: this.editForm.get(['user']).value,
      lab: this.editForm.get(['lab']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIhiwUser>>) {
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

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackIhiwLabById(index: number, item: IIhiwLab) {
    return item.id;
  }
}
