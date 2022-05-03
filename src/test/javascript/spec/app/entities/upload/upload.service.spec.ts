/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { UploadService } from 'app/entities/upload/upload.service';
import { IUpload, Upload, FileType } from 'app/shared/model/upload.model';

describe('Service Tests', () => {
  describe('Upload Service', () => {
    let injector: TestBed;
    let service: UploadService;
    let httpMock: HttpTestingController;
    let elemDefault: IUpload;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(UploadService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Upload(0, FileType.HAML, currentDate, currentDate, 'AAAAAAA', false, 'false');
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            modifiedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        service
          .find(123)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: elemDefault });
      });

      it('should create a Upload', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            modifiedAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdAt: currentDate,
            modifiedAt: currentDate
          },
          returnedFromService
        );
        service
          .create(new Upload(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a Upload', async () => {
        const returnedFromService = Object.assign(
          {
            type: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            modifiedAt: currentDate.format(DATE_TIME_FORMAT),
            fileName: 'BBBBBB',
            valid: true,
            enabled: true
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate,
            modifiedAt: currentDate
          },
          returnedFromService
        );
        service
          .update(expected, null)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of Upload', async () => {
        const returnedFromService = Object.assign(
          {
            type: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT),
            modifiedAt: currentDate.format(DATE_TIME_FORMAT),
            fileName: 'BBBBBB',
            valid: true,
            enabled: true
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdAt: currentDate,
            modifiedAt: currentDate
          },
          returnedFromService
        );
        service
          .query(expected)
          .pipe(
            take(1),
            map(resp => resp.body)
          )
          .subscribe(body => (expectedResult = body));
        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Upload', async () => {
        const rxPromise = service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
