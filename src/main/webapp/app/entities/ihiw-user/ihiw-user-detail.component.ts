import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIhiwUser } from 'app/shared/model/ihiw-user.model';

@Component({
  selector: 'jhi-ihiw-user-detail',
  templateUrl: './ihiw-user-detail.component.html'
})
export class IhiwUserDetailComponent implements OnInit {
  ihiwUser: IIhiwUser;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ ihiwUser }) => {
      this.ihiwUser = ihiwUser;
    });
  }

  previousState() {
    window.history.back();
  }
}
