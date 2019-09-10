/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { IhiwManagementTestModule } from '../../../test.module';
import { UploadDetailComponent } from 'app/entities/upload/upload-detail.component';
import { Upload } from 'app/shared/model/upload.model';

describe('Component Tests', () => {
  describe('Upload Management Detail Component', () => {
    let comp: UploadDetailComponent;
    let fixture: ComponentFixture<UploadDetailComponent>;
    const route = ({ data: of({ upload: new Upload(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [UploadDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UploadDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UploadDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN

        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.upload).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
