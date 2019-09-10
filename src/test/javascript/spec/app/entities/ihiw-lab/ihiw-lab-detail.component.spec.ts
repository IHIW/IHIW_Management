/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IhiwManagementTestModule } from '../../../test.module';
import { IhiwLabDetailComponent } from 'app/entities/ihiw-lab/ihiw-lab-detail.component';
import { IhiwLab } from 'app/shared/model/ihiw-lab.model';

describe('Component Tests', () => {
  describe('IhiwLab Management Detail Component', () => {
    let comp: IhiwLabDetailComponent;
    let fixture: ComponentFixture<IhiwLabDetailComponent>;
    const route = ({ data: of({ ihiwLab: new IhiwLab(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [IhiwLabDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(IhiwLabDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IhiwLabDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.ihiwLab).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
