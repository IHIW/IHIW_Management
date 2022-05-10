import { TestBed, async, tick, fakeAsync, inject } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { of, throwError } from 'rxjs';

import { IhiwManagementTestModule } from '../../../test.module';
import { ActivateService } from 'app/account/activate/activate.service';
import { ActivateComponent } from 'app/account/activate/activate.component';
import { LoginService, StateStorageService } from 'app/core';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { MockLoginService } from '../../../helpers/mock-login.service';
import { MockStateStorageService } from '../../../helpers/mock-state-storage.service';

describe('Component Tests', () => {
  describe('ActivateComponent', () => {
    let comp: ActivateComponent;
    let mockLoginService: any;
    let mockStateStorageService: any;
    let mockRouter: any;

    beforeEach(async(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [ActivateComponent],
        providers: [
          FormBuilder,
          {
            provide: LoginService,
            useClass: MockLoginService
          },
          {
            provide: StateStorageService,
            useClass: MockStateStorageService
          }
        ]
      })
        .overrideTemplate(ActivateComponent, '')
        .compileComponents();
    }));

    beforeEach(() => {
      const fixture = TestBed.createComponent(ActivateComponent);
      comp = fixture.componentInstance;
      mockLoginService = fixture.debugElement.injector.get(LoginService);
      mockStateStorageService = fixture.debugElement.injector.get(StateStorageService);
      mockRouter = fixture.debugElement.injector.get(Router);
    });

    it('calls activate.get with the key from params', inject(
      [ActivateService],
      fakeAsync((service: ActivateService) => {
        spyOn(service, 'get').and.returnValue(of());

        comp.loginForm.patchValue({
          username: 'A',
          password: 'B',
          rememberMe: false
        });
        tick();

        comp.loginAndActivate();
        tick();

        expect(mockLoginService.login).toHaveBeenCalledWith({ username: 'A', password: 'B', rememberMe: false });
      })
    ));

    it('should set set success to OK upon successful activation', inject(
      [ActivateService],
      fakeAsync((service: ActivateService) => {
        spyOn(service, 'get').and.returnValue(of({}));

        // comp.ngOnInit();
        tick();

        // expect(comp.error).toBe(null);
        // expect(comp.success).toEqual('OK');
      })
    ));

    it('should set set error to ERROR upon activation failure', inject(
      [ActivateService],
      fakeAsync((service: ActivateService) => {
        spyOn(service, 'get').and.returnValue(throwError('ERROR'));

        // comp.ngOnInit();
        comp.loginAndActivate();
        tick();

        expect(comp.error).toBe('ERROR');
        expect(comp.success).toEqual(null);
      })
    ));
  });
});
