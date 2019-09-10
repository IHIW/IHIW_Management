import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IhiwManagementSharedModule } from 'app/shared';
import {
  IhiwUserComponent,
  IhiwUserDetailComponent,
  IhiwUserUpdateComponent,
  IhiwUserDeletePopupComponent,
  IhiwUserDeleteDialogComponent,
  ihiwUserRoute,
  ihiwUserPopupRoute
} from './';

const ENTITY_STATES = [...ihiwUserRoute, ...ihiwUserPopupRoute];

@NgModule({
  imports: [IhiwManagementSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    IhiwUserComponent,
    IhiwUserDetailComponent,
    IhiwUserUpdateComponent,
    IhiwUserDeleteDialogComponent,
    IhiwUserDeletePopupComponent
  ],
  entryComponents: [IhiwUserComponent, IhiwUserUpdateComponent, IhiwUserDeleteDialogComponent, IhiwUserDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IhiwManagementIhiwUserModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
