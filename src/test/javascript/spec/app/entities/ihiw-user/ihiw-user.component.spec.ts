/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { IhiwManagementTestModule } from '../../../test.module';
import { IhiwUserComponent } from 'app/entities/ihiw-user/ihiw-user.component';
import { IhiwUserService } from 'app/entities/ihiw-user/ihiw-user.service';
import { IhiwUser } from 'app/shared/model/ihiw-user.model';

describe('Component Tests', () => {
  describe('IhiwUser Management Component', () => {
    let comp: IhiwUserComponent;
    let fixture: ComponentFixture<IhiwUserComponent>;
    let service: IhiwUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [IhiwUserComponent],
        providers: []
      })
        .overrideTemplate(IhiwUserComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IhiwUserComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IhiwUserService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new IhiwUser(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.ihiwUsers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
