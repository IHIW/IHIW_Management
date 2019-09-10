/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { IhiwManagementTestModule } from '../../../test.module';
import { IhiwUserDeleteDialogComponent } from 'app/entities/ihiw-user/ihiw-user-delete-dialog.component';
import { IhiwUserService } from 'app/entities/ihiw-user/ihiw-user.service';

describe('Component Tests', () => {
  describe('IhiwUser Management Delete Component', () => {
    let comp: IhiwUserDeleteDialogComponent;
    let fixture: ComponentFixture<IhiwUserDeleteDialogComponent>;
    let service: IhiwUserService;
    let mockEventManager: any;
    let mockActiveModal: any;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [IhiwManagementTestModule],
        declarations: [IhiwUserDeleteDialogComponent]
      })
        .overrideTemplate(IhiwUserDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(IhiwUserDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(IhiwUserService);
      mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
      mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));
    });
  });
});
