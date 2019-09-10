import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IUpload } from 'app/shared/model/upload.model';

@Component({
  selector: 'jhi-upload-detail',
  templateUrl: './upload-detail.component.html'
})
export class UploadDetailComponent implements OnInit {
  upload: IUpload;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ upload }) => {
      this.upload = upload;
    });
  }

  previousState() {
    window.history.back();
  }
}
