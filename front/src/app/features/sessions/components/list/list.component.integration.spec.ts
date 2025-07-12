import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ListComponent } from './list.component';
import { SessionApiService } from '../../services/session-api.service';
import { SessionService } from '../../../../services/session.service';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';
import { expect } from '@jest/globals';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('ListComponent (integration)', () => {
  let fixture: ComponentFixture<ListComponent>;
  let component: ListComponent;
  let sessionService: SessionService;
  let httpMock: HttpTestingController;

  const mockSessions = [
    {
      id: 1,
      name: 'Yoga Class',
      description: 'Relaxing yoga session',
      date: new Date('2025-07-12T09:00:00'),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];
  const mockSessionInformationUserAdmin = {
    token: 'token',
    type: 'Bearer',
    id: 1,
    username: 'admin',
    firstName: 'Admin',
    lastName: 'User',
    admin: true
  };

  const mockSessionInformationUserNotAdmin = {
    token: 'token',
    type: 'Bearer',
    id: 1,
    username: 'username',
    firstName: 'User',
    lastName: 'User',
    admin: false
  };


  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [HttpClientTestingModule],
      providers: [SessionApiService, SessionService],
      schemas: [CUSTOM_ELEMENTS_SCHEMA] // Ignore les composants Angular Material ici
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should display sessions from API', () => {
    // Simule un utilisateur connecté (admin ou pas ici peu importe)
    sessionService.sessionInformation = mockSessionInformationUserAdmin;

    fixture.detectChanges(); // ngOnInit

    const req = httpMock.expectOne('api/session');
    req.flush(mockSessions);

    fixture.detectChanges(); // actualiser le DOM après réponse

    const sessionTitles = fixture.debugElement.queryAll(By.css('.item mat-card-title'));
    expect(sessionTitles.length).toBe(1);
    expect(sessionTitles[0].nativeElement.textContent).toContain('Yoga Class');
  });

  it('should show "Create" and "Edit" buttons if user is admin', () => {
    sessionService.sessionInformation = mockSessionInformationUserAdmin;

    fixture.detectChanges();

    const req = httpMock.expectOne('api/session');
    req.flush(mockSessions);

    fixture.detectChanges();

     const buttons = fixture.debugElement.queryAll(By.css('button'));

    const createButton = buttons.find(b =>
        b.nativeElement.textContent.includes('Create')
    );
    
    const editButton = buttons.find(b =>
        b.nativeElement.textContent.includes('Edit')
    );

  expect(createButton).toBeTruthy();
  expect(editButton).toBeTruthy();
  });

  it('should not show "Create" and "Edit" buttons if user is not admin', () => {
    sessionService.sessionInformation = mockSessionInformationUserNotAdmin;

    fixture.detectChanges();

    const req = httpMock.expectOne('api/session');
    req.flush(mockSessions);

    fixture.detectChanges();

    const buttons = fixture.debugElement.queryAll(By.css('button'));

    const createButton = buttons.find(b =>
        b.nativeElement.textContent.includes('Create')
    );
    
    const editButton = buttons.find(b =>
        b.nativeElement.textContent.includes('Edit')
    );

    expect(createButton).toBeFalsy();
    expect(editButton).toBeFalsy();
  });
});