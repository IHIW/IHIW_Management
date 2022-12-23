import { Component, OnInit, NgModule } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MarkdownModule } from 'ngx-markdown';

import { IProject, Project } from 'app/shared/model/project.model';
import { JhiEventManager } from 'ng-jhipster';
import { Observable, Subscription } from 'rxjs';
import { ProjectService } from 'app/entities/project/project.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-project-detail',
  templateUrl: './project-detail.component.html'
})
@NgModule({
  imports: [MarkdownModule.forChild()]
})
export class ProjectDetailComponent implements OnInit {
  project: IProject;

  constructor(
    protected projectService: ProjectService,
    protected activatedRoute: ActivatedRoute,
    protected eventManager: JhiEventManager
  ) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ project }) => {
      this.project = project;
    });
  }

  previousState() {
    window.history.back();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    console.log('success!');
    this.previousState();
  }

  protected onSaveError() {
    console.log('error!');
    this.previousState();
  }

  createProjectZip() {
    this.subscribeToSaveResponse(this.projectService.createProjectZip(this.project));
  }
}
