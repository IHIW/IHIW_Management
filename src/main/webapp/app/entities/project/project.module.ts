import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { JhiLanguageService } from 'ng-jhipster';
import { JhiLanguageHelper } from 'app/core';
import { MarkdownModule } from 'ngx-markdown';
import { IhiwManagementSharedModule } from 'app/shared';
import { StatusPipe } from 'app/status.pipe';
import {
  ProjectComponent,
  ProjectDetailComponent,
  ProjectUpdateComponent,
  ProjectDeletePopupComponent,
  ProjectDeleteDialogComponent,
  ProjectSubscribePopupComponent,
  ProjectSubscribeDialogComponent,
  ProjectUnsubscribePopupComponent,
  ProjectUnsubscribeDialogComponent,
  projectRoute,
  projectPopupRoute
} from './';

const ENTITY_STATES = [...projectRoute, ...projectPopupRoute];

@NgModule({
  imports: [IhiwManagementSharedModule, RouterModule.forChild(ENTITY_STATES), MarkdownModule.forChild()],
  declarations: [
    ProjectComponent,
    ProjectDetailComponent,
    ProjectUpdateComponent,
    ProjectDeleteDialogComponent,
    ProjectDeletePopupComponent,
    ProjectSubscribeDialogComponent,
    ProjectSubscribePopupComponent,
    ProjectUnsubscribeDialogComponent,
    ProjectUnsubscribePopupComponent,
    StatusPipe
  ],
  entryComponents: [
    ProjectComponent,
    ProjectUpdateComponent,
    ProjectDeleteDialogComponent,
    ProjectDeletePopupComponent,
    ProjectSubscribeDialogComponent,
    ProjectSubscribePopupComponent,
    ProjectUnsubscribeDialogComponent,
    ProjectUnsubscribePopupComponent
  ],
  providers: [{ provide: JhiLanguageService, useClass: JhiLanguageService }],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IhiwManagementProjectModule {
  constructor(private languageService: JhiLanguageService, private languageHelper: JhiLanguageHelper) {
    this.languageHelper.language.subscribe((languageKey: string) => {
      if (languageKey) {
        this.languageService.changeLanguage(languageKey);
      }
    });
  }
}
