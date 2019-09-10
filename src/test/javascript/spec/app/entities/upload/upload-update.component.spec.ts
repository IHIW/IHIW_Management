/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { IhiwManagementTestModule } from '../../../test.module';
import { UploadUpdateComponent } from 'app/entities/upload/upload-update.component';
import { UploadService } from 'app/entities/upload/upload.service';
import { Upload } from 'app/shared/model/upload.model';

describe('Component Tests', () => {
  describe('Upload Management Update Component', () => {
    let comp: UploadUpdateComponent;
    let fixture: ComponentFixture<UploadUpdateComponent>;
    let service: UploadService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [UploadUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(UploadUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UploadUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UploadService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Upload(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Upload();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        // expect(service.create).toHaveBeenCalledWith(entity);
        // expect(comp.isSaving).toEqual(false);
        // FIXME
      }));
    });
  });
});
