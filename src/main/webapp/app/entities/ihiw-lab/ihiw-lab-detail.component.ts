import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIhiwLab } from 'app/shared/model/ihiw-lab.model';

@Component({
  selector: 'jhi-ihiw-lab-detail',
  templateUrl: './ihiw-lab-detail.component.html'
})
export class IhiwLabDetailComponent implements OnInit {
  ihiwLab: IIhiwLab;
  legacy: Boolean;
  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.legacy = false;
    this.activatedRoute.data.subscribe(({ ihiwLab }) => {
      this.ihiwLab = ihiwLab;
    });
  }

  previousState() {
    window.history.back();
  }

  toggleLegacyFields() {
    this.legacy = !this.legacy;
  }
}
