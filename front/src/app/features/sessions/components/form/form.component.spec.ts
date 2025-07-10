import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {  FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { FormComponent } from './form.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { TeacherService } from 'src/app/services/teacher.service';
import { Session } from '../../interfaces/session.interface';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let httpMock: HttpTestingController;
  let sessionMock: Session = {
    id: 1,
    name: 'Session',
    description: 'Description',
    date: new Date('2025-01-01'),
    teacher_id: 1,
    users: [],
    createdAt: new Date('2025-01-01'),
    updatedAt: new Date('2025-01-01'),
  };
  const mockRoute = { 
    snapshot: { 
      paramMap: { 
        get: jest.fn().mockReturnValue('1') 
      } 
    } 
  };
  
  const mockMatSnackBar = { 
    open: jest.fn() 
  };
  
  const mockSessionService = {
    sessionInformation: {
      admin: true,
    },
    detail: jest.fn(),
    create: jest.fn(),
    update: jest.fn(),
  };

  const mockTeacherService = { 
    all: jest.fn().mockReturnValue(of([])) 
  };
  
  const mockRouter = {
    url: '/sessions/create',
    navigate: jest.fn(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule,
      ],
      providers: [
        { provide: ActivatedRoute, useValue: mockRoute },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: SessionService, useValue: mockSessionService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: Router, useValue: mockRouter },
        FormBuilder,
      ],
      declarations: [FormComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    jest.clearAllMocks();
    const requests = httpMock.match(() => true);
    requests.forEach(req => req.flush({}));
    httpMock.verify();
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  describe('When filling the form to create a new Session', () => {
    it('should initialize the form with empty values', () => {
      fixture.detectChanges();
      component.ngOnInit();  
  
      expect(component.sessionForm).toBeDefined();
      expect(component.sessionForm?.value).toEqual({
        name: '',
        date: '',
        teacher_id: '',
        description: '',
      });
    });

    it('should send a POST request when all fields are filled after submit', () => {
      fixture.detectChanges();

      component.sessionForm?.controls['name'].setValue(sessionMock.name);
      component.sessionForm?.controls['date'].setValue(sessionMock.date.toISOString().split('T')[0]);
      component.sessionForm?.controls['teacher_id'].setValue(sessionMock.teacher_id);
      component.sessionForm?.controls['description'].setValue(sessionMock.description);
      component.onUpdate = false;
      component.submit();

      const req = httpMock.expectOne('api/session');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual({
        name: sessionMock.name,
        date: sessionMock.date.toISOString().split('T')[0],
        teacher_id: sessionMock.teacher_id,
        description: sessionMock.description,
      });

      req.flush(sessionMock);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session created !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('When updating the information of a session', () => {
    it('should call update() from sessionApiService when having valid data', () => {
      const sessionId = '1';
      sessionMock.description = 'new Desc';

      mockRoute.snapshot.paramMap.get.mockReturnValue(sessionId);
      mockRouter.url = '/sessions/update';

      const mockSessionApiService = TestBed.inject(SessionApiService);
      jest.spyOn(mockSessionApiService, 'detail').mockReturnValue(of(sessionMock));

      // Déclencher ngOnInit
      fixture.detectChanges();
      component.onUpdate = true; 

      component.sessionForm?.controls['name'].setValue(sessionMock.name);
      component.sessionForm?.controls['date'].setValue(sessionMock.date.toISOString().split('T')[0]);
      component.sessionForm?.controls['teacher_id'].setValue(sessionMock.teacher_id);
      component.sessionForm?.controls['description'].setValue(sessionMock.description);
      component.submit();

      const req = httpMock.expectOne(`api/session/${sessionId}`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual({
        name: sessionMock.name,
        date: sessionMock.date.toISOString().split('T')[0],
        teacher_id: sessionMock.teacher_id,
        description: 'new Desc',
      });

      req.flush(sessionMock);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session updated !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });
  
  describe('When exitPage() is called', () => {
    it('should show a notification and go to /sessions page', () => {
      const message = 'Session created!';
      component['exitPage'](message);
      expect(mockMatSnackBar.open).toHaveBeenCalledWith(message, 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });
});
