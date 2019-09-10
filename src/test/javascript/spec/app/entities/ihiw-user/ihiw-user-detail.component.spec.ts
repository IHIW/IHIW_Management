/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IhiwManagementTestModule } from '../../../test.module';
import { IhiwUserDetailComponent } from 'app/entities/ihiw-user/ihiw-user-detail.component';
import { IhiwUser } from 'app/shared/model/ihiw-user.model';

describe('Component Tests', () => {
  describe('IhiwUser Management Detail Component', () => {
    let comp: IhiwUserDetailComponent;
    let fixture: ComponentFixture<IhiwUserDetailComponent>;
    const route = ({ data: of({ ihiwUser: new IhiwUser(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [IhiwUserDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IhiwUserDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IhiwUserDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ihiwUser).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
