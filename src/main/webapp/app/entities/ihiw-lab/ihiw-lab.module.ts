import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';

import { IhiwManagementSharedModule } from 'app/shared';
import {
  IhiwLabComponent,
  IhiwLabDetailComponent,
  IhiwLabUpdateComponent,
  IhiwLabDeletePopupComponent,
  IhiwLabDeleteDialogComponent,
  ihiwLabRoute,
  ihiwLabPopupRoute
} from './';

const ENTITY_STATES = [...ihiwLabRoute, ...ihiwLabPopupRoute];

@NgModule({
  imports: [IhiwManagementSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    IhiwLabComponent,
    IhiwLabDetailComponent,
    IhiwLabUpdateComponent,
    IhiwLabDeleteDialogComponent,
    IhiwLabDeletePopupComponent
  ],
  entryComponents: [IhiwLabComponent, IhiwLabUpdateComponent, IhiwLabDeleteDialogComponent, IhiwLabDeletePopupComponent],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IhiwManagementIhiwLabModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
