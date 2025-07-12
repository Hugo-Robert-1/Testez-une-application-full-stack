import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { FormComponent } from './form.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { TeacherService } from '../../../../services/teacher.service';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { expect } from '@jest/globals';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon'
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

describe('FormComponent (integration)', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  const mockSession = {
    id: 1,
    name: 'Session A',
    description: 'Description A',
    date: '2025-07-20T00:00:00Z',
    teacher_id: 2,
    users: [],
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockTeachers = [
    { id: 1, firstName: 'Alice', lastName: 'Doe' },
    { id: 2, firstName: 'Bob', lastName: 'Smith' }
  ];

  const teacherServiceMock = {
    all: () => of(mockTeachers)  // retourne un Observable immédiat avec mockTeachers
  };

  describe('FormComponent create mode', () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [FormComponent],
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          MatSnackBarModule,
          MatCardModule,
          MatIconModule,
          NoopAnimationsModule
        ],
        providers: [
          SessionApiService,
          TeacherService,
          {
            provide: SessionService,
            useValue: { sessionInformation: {
              token: 'token',
              type: 'Bearer',
              id: 1,
              username: 'username',
              firstName: 'User',
              lastName: 'User',
              admin: true
            }}
          },
          {
            provide: ActivatedRoute,
            useValue: {
              snapshot: {
                paramMap: {
                  get: () => null // pas d'id = create mode
                }
              }
            }
          },
          {
            provide: Router,
            useValue: {
              url: '/sessions/create',
              navigate: jest.fn()
            }
          }
        ]
      }).compileComponents();

      fixture = TestBed.createComponent(FormComponent);
      component = fixture.componentInstance;
      httpMock = TestBed.inject(HttpTestingController);
      router = TestBed.inject(Router);
    });

    afterEach(() => {
      httpMock.verify();
    });

    it('should initialize in create mode', () => {
      fixture.detectChanges();
  
      const req = httpMock.expectOne('api/teacher');
      req.flush(mockTeachers);
  
      fixture.detectChanges();

      expect(component.onUpdate).toBe(false);
      expect(component.sessionForm).toBeTruthy();
    });

    it('should submit form and call create() on submit', fakeAsync(() => {
    fixture.detectChanges();

    const teacherReq = httpMock.expectOne('api/teacher');
    teacherReq.flush(mockTeachers);

    component.sessionForm?.setValue({
      name: 'New session',
      date: '2025-08-01',
      teacher_id: 2,
      description: 'A test session'
    });

    const navigateSpy = jest.spyOn(router, 'navigate');

    component.submit();

    const createReq = httpMock.expectOne('api/session');
    expect(createReq.request.method).toBe('POST');
    createReq.flush({ ...mockSession, name: 'New session' });

    tick(3500); // pour matSnackBar et navigate

    expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
  }));

  });

  describe('FormComponent update mode', () => {
    beforeEach(async () => {
      await TestBed.configureTestingModule({
        declarations: [FormComponent],
        imports: [
          ReactiveFormsModule,
          HttpClientTestingModule,
          MatSnackBarModule,
          MatCardModule,
          MatIconModule,
        ],
        providers: [
          SessionApiService,
          {
            provide: TeacherService,
            useValue: teacherServiceMock
          },
          {
            provide: SessionService,
            useValue: { sessionInformation: {
              token: 'token',
              type: 'Bearer',
              id: 1,
              username: 'username',
              firstName: 'User',
              lastName: 'User',
              admin: true
            } }
          },
          {
            provide: ActivatedRoute,
            useValue: {
              snapshot: {
                paramMap: {
                  get: () => '1' // id présent = update mode
                }
              }
            }
          },
          {
            provide: Router,
            useValue: {
              get url() { return '/sessions/update/1'; },
              navigate: jest.fn()
            }
          }
        ]
      }).compileComponents();
  
      fixture = TestBed.createComponent(FormComponent);
      component = fixture.componentInstance;
      httpMock = TestBed.inject(HttpTestingController);
      router = TestBed.inject(Router);
    });

    afterEach(() => {
      httpMock.verify();
    });
  
    it('should initialize in update mode and load session', () => {
      const spy = jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update/1');
  
      fixture.detectChanges();
  
      const sessionReq = httpMock.expectOne('api/session/1');
      sessionReq.flush(mockSession);
    
      fixture.detectChanges();
  
      expect(component.onUpdate).toBe(true);
      expect(component.sessionForm?.value.name).toBe('Session A');
  
      spy.mockRestore();
    });
 /**
    it('should submit form and call update() on submit', fakeAsync(() => {
      // Simuler "update" dans l'URL
      const route = TestBed.inject(ActivatedRoute);
      route.snapshot.paramMap.get = () => '1';
      const router = TestBed.inject(Router);
      const navigateSpy = jest.spyOn(router, 'navigate');
  
      fixture.detectChanges();
  
      const teacherReq = httpMock.expectOne('api/teacher');
      teacherReq.flush(mockTeachers);
  
      const detailReq = httpMock.expectOne('api/session/1');
      detailReq.flush(mockSession);
  
      fixture.detectChanges();
  
      component.sessionForm?.setValue({
        name: 'Updated session',
        date: '2025-08-01',
        teacher_id: 2,
        description: 'Updated description'
      });
  
      component.submit();
  
      const updateReq = httpMock.expectOne('api/session/1');
      expect(updateReq.request.method).toBe('PUT');
      updateReq.flush({ ...mockSession, name: 'Updated session' });
  
      tick();
      expect(navigateSpy).toHaveBeenCalledWith(['sessions']);
    }));**/
  });
});