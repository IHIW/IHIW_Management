/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Observable, of } from 'rxjs';

import { IhiwManagementTestModule } from '../../../test.module';
import { IhiwUserUpdateComponent } from 'app/entities/ihiw-user/ihiw-user-update.component';
import { IhiwUserService } from 'app/entities/ihiw-user/ihiw-user.service';
import { IhiwUser } from 'app/shared/model/ihiw-user.model';

describe('Component Tests', () => {
  describe('IhiwUser Management Update Component', () => {
    let comp: IhiwUserUpdateComponent;
    let fixture: ComponentFixture<IhiwUserUpdateComponent>;
    let service: IhiwUserService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [IhiwUserUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(IhiwUserUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(IhiwUserUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IhiwUserService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new IhiwUser(123);
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
        const entity = new IhiwUser();
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
