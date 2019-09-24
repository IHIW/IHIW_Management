import { Component, OnInit, NgModule } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MarkdownModule } from 'ngx-markdown';

import { IProject } from 'app/shared/model/project.model';

@Component({
  selector: 'jhi-project-detail',
  templateUrl: './project-detail.component.html'
})
@NgModule({
  imports: [MarkdownModule.forChild()]
})
export class ProjectDetailComponent implements OnInit {
  project: IProject;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ project }) => {
      this.project = project;
    });
  }

  previousState() {
    window.history.back();
  }
}
