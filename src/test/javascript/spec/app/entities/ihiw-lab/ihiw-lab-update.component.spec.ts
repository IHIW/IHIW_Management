/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { IhiwManagementTestModule } from '../../../test.module';
import { IhiwLabUpdateComponent } from 'app/entities/ihiw-lab/ihiw-lab-update.component';
import { IhiwLabService } from 'app/entities/ihiw-lab/ihiw-lab.service';
import { IhiwLab } from 'app/shared/model/ihiw-lab.model';

describe('Component Tests', () => {
  describe('IhiwLab Management Update Component', () => {
    let comp: IhiwLabUpdateComponent;
    let fixture: ComponentFixture<IhiwLabUpdateComponent>;
    let service: IhiwLabService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [IhiwLabUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IhiwLabUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IhiwLabUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IhiwLabService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new IhiwLab(123);
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
        const entity = new IhiwLab();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
