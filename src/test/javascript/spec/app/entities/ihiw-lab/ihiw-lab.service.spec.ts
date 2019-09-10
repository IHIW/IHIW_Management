/* tslint:disable max-line-length */
import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { IhiwLabService } from 'app/entities/ihiw-lab/ihiw-lab.service';
import { IIhiwLab, IhiwLab } from 'app/shared/model/ihiw-lab.model';

describe('Service Tests', () => {
  describe('IhiwLab Service', () => {
    let injector: TestBed;
    let service: IhiwLabService;
    let httpMock: HttpTestingController;
    let elemDefault: IIhiwLab;
    let expectedResult;
    let currentDate: moment.Moment;
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = {};
      injector = getTestBed();
      service = injector.get(IhiwLabService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new IhiwLab(
        0,
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        'AAAAAAA',
        currentDate
      );
    });

    describe('Service methods', () => {
      it('should find an element', async () => {
        const returnedFromService = Object.assign(
          {
            createdAt: currentDate.format(DATE_TIME_FORMAT)
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

      it('should create a IhiwLab', async () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            createdAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdAt: currentDate
          },
          returnedFromService
        );
        service
          .create(new IhiwLab(null))
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should update a IhiwLab', async () => {
        const returnedFromService = Object.assign(
          {
            labCode: 'BBBBBB',
            title: 'BBBBBB',
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            director: 'BBBBBB',
            department: 'BBBBBB',
            institution: 'BBBBBB',
            address1: 'BBBBBB',
            address2: 'BBBBBB',
            sAddress1: 'BBBBBB',
            sAddress: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            zip: 'BBBBBB',
            country: 'BBBBBB',
            phone: 'BBBBBB',
            fax: 'BBBBBB',
            email: 'BBBBBB',
            url: 'BBBBBB',
            oldLabCode: 'BBBBBB',
            sName: 'BBBBBB',
            sPhone: 'BBBBBB',
            sEmail: 'BBBBBB',
            dName: 'BBBBBB',
            dEmail: 'BBBBBB',
            dPhone: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            createdAt: currentDate
          },
          returnedFromService
        );
        service
          .update(expected)
          .pipe(take(1))
          .subscribe(resp => (expectedResult = resp));
        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject({ body: expected });
      });

      it('should return a list of IhiwLab', async () => {
        const returnedFromService = Object.assign(
          {
            labCode: 'BBBBBB',
            title: 'BBBBBB',
            firstName: 'BBBBBB',
            lastName: 'BBBBBB',
            director: 'BBBBBB',
            department: 'BBBBBB',
            institution: 'BBBBBB',
            address1: 'BBBBBB',
            address2: 'BBBBBB',
            sAddress1: 'BBBBBB',
            sAddress: 'BBBBBB',
            city: 'BBBBBB',
            state: 'BBBBBB',
            zip: 'BBBBBB',
            country: 'BBBBBB',
            phone: 'BBBBBB',
            fax: 'BBBBBB',
            email: 'BBBBBB',
            url: 'BBBBBB',
            oldLabCode: 'BBBBBB',
            sName: 'BBBBBB',
            sPhone: 'BBBBBB',
            sEmail: 'BBBBBB',
            dName: 'BBBBBB',
            dEmail: 'BBBBBB',
            dPhone: 'BBBBBB',
            createdAt: currentDate.format(DATE_TIME_FORMAT)
          },
          elemDefault
        );
        const expected = Object.assign(
          {
            createdAt: currentDate
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

      it('should delete a IhiwLab', async () => {
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
