import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Form, FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals'; 
import { SessionService } from '../../../../services/session.service';

import { DetailComponent } from './detail.component';
import { of, throwError } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { ActivatedRoute, Router } from '@angular/router';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from 'src/app/services/teacher.service';
import { Teacher } from 'src/app/interfaces/teacher.interface';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>; 
  let service: SessionService;
  let mockRoute: any = { snapshot:
    { paramMap: 
      { get: jest.fn(() => '1') }
    }
  };
  let mockFb: FormBuilder = new FormBuilder();

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }
  
  let mockSessionApiService : any ={
    detail: jest.fn().mockReturnValue(of({})),
    participate: jest.fn().mockReturnValue(of({})),
    unParticipate: jest.fn().mockReturnValue(of({})),
    delete: jest.fn().mockReturnValue(of({}))
  }

  let mockTeacherService : any = {
    detail: jest.fn().mockReturnValue(of({}))
  }

  let mockMatSnackBar: any = {
    open: jest.fn()
  }

  let mockRouter: any = {
    navigate: jest.fn()
  }

  const mockSession : Session = {
    id: 1,
    name: 'Test Session',
    description: 'This is a test session',
    date: new Date(),
    teacher_id: 1,
    users: [1, 2],
    createdAt: new Date(),
    updatedAt: new Date()
  }

  const mockTeacher: Teacher = {
    id: 1,
    firstName: 'John',
    lastName: 'Smith',
    createdAt:  new Date(),
    updatedAt:  new Date(),
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatIconModule,
        MatCardModule
      ],
      declarations: [DetailComponent], 
      providers: [
        { provide: ActivatedRoute, useValue: mockRoute },
        { provide: FormBuilder, useValue: mockFb },
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ],
    }).compileComponents();

    service = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;

    // Mocking the session and teacher details
    mockSessionApiService.detail.mockReturnValue(of(mockSession));
    mockTeacherService.detail.mockReturnValue(of(mockTeacher));

    component.ngOnInit();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('Delete Session', () => {
    it('should delete the session and navigate to sessions', () => {
      component.delete();
      expect(mockSessionApiService.delete).toHaveBeenCalledWith('1');
      expect(mockMatSnackBar.open).toHaveBeenCalledWith('Session deleted !', 'Close', { duration: 3000 });
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });
  })

  describe('Participate in Session', () => {
    it('should participate in the session', () => {
      const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
      component.participate();
      expect(mockSessionApiService.participate).toHaveBeenCalledWith(component.sessionId, component.userId);
      expect(mockSessionApiService.detail).toHaveBeenCalledWith(component.sessionId);
      expect(fetchSessionSpy).toHaveBeenCalled();
    });
  });

  describe('Unparticipate from a Session', () => {
    it('should unparticipate in the session', () => {
      const fetchSessionSpy = jest.spyOn(component as any, 'fetchSession');
      component.unParticipate();
      expect(mockSessionApiService.unParticipate).toHaveBeenCalledWith(component.sessionId, component.userId);
      expect(mockSessionApiService.detail).toHaveBeenCalledWith(component.sessionId);
      expect(fetchSessionSpy).toHaveBeenCalled();
    });
  });

  describe('user is not an admin', () => {
    beforeEach(() => {
      mockSessionService.sessionInformation.admin = false;
      component.ngOnInit();
    });

    it('should not allow access to the session detail', () => {
      expect(mockRouter.navigate).toHaveBeenCalledWith(['sessions']);
    });

    it('should show an error message when delete fails', () => {
      jest.spyOn(mockSessionApiService, 'delete').mockReturnValue(throwError(() => new Error('Delete failed')));
    
      component.delete();
    
      expect(mockSessionApiService.delete).toHaveBeenCalledWith(component.sessionId);
      expect(mockMatSnackBar.open).toHaveBeenCalled();
    });
  })

  describe('what the user sees depending on his participation in the session', () => {
    describe('when the user is participating', () => {
      beforeEach(() => {
        component.isParticipate = true;
        fixture.detectChanges();
      });

      it('should show the unparticipate button', () => {
        expect(component.isParticipate).toBe(true);
        const unParticipateButton = fixture.nativeElement.querySelector('button[color="warn"]');
        expect(unParticipateButton).toBeTruthy();
      });
    });

    describe('when the user is not participating', () => {
      beforeEach(() => {
        component.isParticipate = false;
        fixture.detectChanges();
      });

      it('should show the participate button', () => {
        expect(component.isParticipate).toBe(false);
        const participateButton = fixture.nativeElement.querySelector('button[color="primary"]');
        expect(participateButton).toBeTruthy();
      });
    });
  })
});

