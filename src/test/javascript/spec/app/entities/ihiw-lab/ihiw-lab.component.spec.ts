/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { IhiwManagementTestModule } from '../../../test.module';
import { IhiwLabComponent } from 'app/entities/ihiw-lab/ihiw-lab.component';
import { IhiwLabService } from 'app/entities/ihiw-lab/ihiw-lab.service';
import { IhiwLab } from 'app/shared/model/ihiw-lab.model';

describe('Component Tests', () => {
  describe('IhiwLab Management Component', () => {
    let comp: IhiwLabComponent;
    let fixture: ComponentFixture<IhiwLabComponent>;
    let service: IhiwLabService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [IhiwLabComponent],
        providers: []
      })
        .overrideTemplate(IhiwLabComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IhiwLabComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IhiwLabService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new IhiwLab(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.ihiwLabs[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
