import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { EMAIL_ALREADY_USED_TYPE, LOGIN_ALREADY_USED_TYPE } from 'app/shared';
import { LoginModalService } from 'app/core';
import { Register } from './register.service';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit, AfterViewInit {
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorUserExists: string;
  success: boolean;
  modalRef: NgbModalRef;

  registerForm = this.fb.group({
    login: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[_.@A-Za-z0-9-]*$')]],
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    existingLab: ['true', [Validators.required]],
    labCode: ['', [Validators.required]],
    title: [''],
    director: [''],
    department: [''],
    institution: [''],
    address1: [''],
    address2: [''],
    city: [''],
    state: [''],
    zip: [''],
    country: [''],
    phone: [''],
    fax: [''],
    labEmail: [''],
    url: ['']
  });

  constructor(
    private languageService: JhiLanguageService,
    private loginModalService: LoginModalService,
    private registerService: Register,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.success = false;
    this.setRegistrationValidators();
  }

  setRegistrationValidators() {
    const labCodeControl = this.registerForm.get('labCode');
    const labEmailControl = this.registerForm.get('labEmail');
    const titleControl = this.registerForm.get('title');
    const directorControl = this.registerForm.get('director');
    const departmentControl = this.registerForm.get('department');
    const institutionControl = this.registerForm.get('institution');
    const address1Control = this.registerForm.get('address1');
    const cityControl = this.registerForm.get('city');
    const zipControl = this.registerForm.get('zip');
    const countryControl = this.registerForm.get('country');

    this.registerForm.get('existingLab').valueChanges.subscribe(existingLab => {
      if (existingLab === 'true') {
        labCodeControl.setValidators([Validators.required]);
        labEmailControl.setValidators(null);
        titleControl.setValidators(null);
        directorControl.setValidators(null);
        departmentControl.setValidators(null);
        institutionControl.setValidators(null);
        address1Control.setValidators(null);
        cityControl.setValidators(null);
        zipControl.setValidators(null);
        countryControl.setValidators(null);
      }

      if (existingLab === 'false') {
        labCodeControl.setValidators(null);
        labEmailControl.setValidators([Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]);
        titleControl.setValidators([Validators.required]);
        directorControl.setValidators([Validators.required]);
        departmentControl.setValidators([Validators.required]);
        institutionControl.setValidators([Validators.required]);
        address1Control.setValidators([Validators.required]);
        cityControl.setValidators([Validators.required]);
        zipControl.setValidators([Validators.required]);
        countryControl.setValidators([Validators.required]);
      }

      labCodeControl.updateValueAndValidity();
      labEmailControl.updateValueAndValidity();
      titleControl.updateValueAndValidity();
      directorControl.updateValueAndValidity();
      departmentControl.updateValueAndValidity();
      institutionControl.updateValueAndValidity();
      address1Control.updateValueAndValidity();
      cityControl.updateValueAndValidity();
      zipControl.updateValueAndValidity();
      countryControl.updateValueAndValidity();
    });
  }

  ngAfterViewInit() {
    this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#login'), 'focus', []);
  }

  register() {
    let registerAccount = {};
    const login = this.registerForm.get(['login']).value;
    const email = this.registerForm.get(['email']).value;
    const firstName = this.registerForm.get(['firstName']).value;
    const lastName = this.registerForm.get(['lastName']).value;
    const password = this.registerForm.get(['password']).value;
    const labCode = this.registerForm.get(['labCode']).value;
    const title = this.registerForm.get(['title']).value;
    const director = this.registerForm.get(['director']).value;
    const department = this.registerForm.get(['department']).value;
    const institution = this.registerForm.get(['institution']).value;
    const address1 = this.registerForm.get(['address1']).value;
    const address2 = this.registerForm.get(['address2']).value;
    const city = this.registerForm.get(['city']).value;
    const state = this.registerForm.get(['state']).value;
    const zip = this.registerForm.get(['zip']).value;
    const country = this.registerForm.get(['country']).value;
    const phone = this.registerForm.get(['phone']).value;
    const fax = this.registerForm.get(['fax']).value;
    const labEmail = this.registerForm.get(['labEmail']).value;
    const url = this.registerForm.get(['url']).value;
    const existingLab = this.registerForm.get(['existingLab']).value;

    if (password !== this.registerForm.get(['confirmPassword']).value) {
      this.doNotMatch = 'ERROR';
    } else {
      registerAccount = {
        ...registerAccount,
        login,
        email,
        password,
        labCode,
        title,
        director,
        department,
        institution,
        address1,
        address2,
        city,
        state,
        zip,
        country,
        phone,
        fax,
        labEmail,
        url,
        existingLab,
        firstName,
        lastName
      };
      this.doNotMatch = null;
      this.error = null;
      this.errorUserExists = null;
      this.errorEmailExists = null;
      this.languageService.getCurrent().then(langKey => {
        registerAccount = { ...registerAccount, langKey };
        this.registerService.save(registerAccount).subscribe(
          () => {
            this.success = true;
          },
          response => this.processError(response)
        );
      });
    }
  }

  openLogin() {
    this.modalRef = this.loginModalService.open();
  }

  private processError(response: HttpErrorResponse) {
    this.success = null;
    if (response.status === 400 && response.error.type === LOGIN_ALREADY_USED_TYPE) {
      this.errorUserExists = 'ERROR';
    } else if (response.status === 400 && response.error.type === EMAIL_ALREADY_USED_TYPE) {
      this.errorEmailExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }
  }
}
