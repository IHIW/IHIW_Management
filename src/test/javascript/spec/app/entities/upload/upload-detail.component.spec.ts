/* tslint:disable max-line-length */
import { ComponentFixture, getTestBed, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';

import { IhiwManagementTestModule } from '../../../test.module';
import { UploadDetailComponent } from 'app/entities/upload/upload-detail.component';
import { FileType, IUpload, Upload } from 'app/shared/model/upload.model';
import { of } from 'rxjs';
import { DATE_TIME_FORMAT } from 'app/shared';
import * as moment from 'moment';
import { HttpTestingController } from '@angular/common/http/testing';

describe('Component Tests', () => {
  describe('Upload Management Detail Component', () => {
    let injector: TestBed;
    let comp: UploadDetailComponent;
    let fixture: ComponentFixture<UploadDetailComponent>;
    let elemDefault: IUpload;
    let currentDate: moment.Moment;
    let httpMock: HttpTestingController;

    const route = ({
      data: of({ upload: new Upload(123) }),
      snapshot: {
        paramMap: {
          get: () => ({ id: 123 })
        }
      }
    } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [UploadDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(UploadDetailComponent, '')
        .compileComponents();
      injector = getTestBed();
      httpMock = injector.get(HttpTestingController);
      fixture = TestBed.createComponent(UploadDetailComponent);
      comp = fixture.componentInstance;
      currentDate = moment();
      elemDefault = new Upload(123, FileType.HAML, currentDate, currentDate, 'AAAAAAA', false, 'false');
    });

    describe('OnInit', () => {
      it('Should call load all on init', () => {
        // GIVEN
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            modifiedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        // WHEN
        comp.ngOnInit();

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);

        // THEN
        expect(comp.upload).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
