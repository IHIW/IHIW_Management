import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { ActivateService } from './activate.service';
import { FormBuilder } from '@angular/forms';

import { LoginService } from 'app/core/login/login.service';
import { JhiEventManager } from 'ng-jhipster';

@Component({
  selector: 'jhi-activate',
  templateUrl: './activate.component.html'
})
export class ActivateComponent {
  error: string;
  success: string;
  authenticationError: boolean;

  loginForm = this.fb.group({
    username: [''],
    password: [''],
    rememberMe: [false]
  });

  constructor(
    private eventManager: JhiEventManager,
    private activateService: ActivateService,
    private route: ActivatedRoute,
    private loginService: LoginService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  loginAndActivate() {
    this.loginService
      .login({
        username: this.loginForm.get('username').value,
        password: this.loginForm.get('password').value,
        rememberMe: this.loginForm.get('rememberMe').value
      })
      .then(() => {
        this.eventManager.broadcast({
          name: 'authenticationSuccess',
          content: 'Sending Authentication Success'
        });

        this.route.queryParams.subscribe(params => {
          this.activateService.get(params['key']).subscribe(
            () => {
              this.error = null;
              this.success = 'OK';
            },
            () => {
              this.success = null;
              this.error = 'ERROR';
            }
          );
        });
      })
      .catch(() => {
        this.success = null;
        this.error = 'ERROR';
      });
  }
}
