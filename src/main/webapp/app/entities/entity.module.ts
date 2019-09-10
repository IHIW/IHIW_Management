import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'project',
        loadChildren: () => import('./project/project.module').then(m => m.IhiwManagementProjectModule)
      },
      {
        path: 'upload',
        loadChildren: () => import('./upload/upload.module').then(m => m.IhiwManagementUploadModule)
      },
      {
        path: 'ihiw-lab',
        loadChildren: () => import('./ihiw-lab/ihiw-lab.module').then(m => m.IhiwManagementIhiwLabModule)
      },
      {
        path: 'ihiw-user',
        loadChildren: () => import('./ihiw-user/ihiw-user.module').then(m => m.IhiwManagementIhiwUserModule)
      }
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ])
  ],
  declarations: [],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class IhiwManagementEntityModule {}
