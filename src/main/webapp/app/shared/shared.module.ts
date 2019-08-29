import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { IhiwManagementSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [IhiwManagementSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [IhiwManagementSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IhiwManagementSharedModule {
  static forRoot() {
    return {
      ngModule: IhiwManagementSharedModule
    };
  }
}
