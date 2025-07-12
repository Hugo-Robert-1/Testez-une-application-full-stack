import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { DetailComponent } from './detail.component';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { SessionService } from '../../../../services/session.service';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { expect } from '@jest/globals';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { FormBuilder } from '@angular/forms';

describe('DetailComponent (integration)', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let httpMock: HttpTestingController;
  let sessionApiService: SessionApiService;
  let teacherService: TeacherService;
  let sessionService: SessionService;
  let router: Router;
  let mockFb: FormBuilder = new FormBuilder();

  const mockSession = {
    id: 1,
    name: 'Test Session',
    description: 'Description test',
    date: new Date().toISOString(),
    teacher_id: 42,
    users: [99],
    createdAt: new Date().toISOString(),
    updatedAt: new Date().toISOString()
  };

  const mockTeacher = {
    id: 42,
    firstName: 'Jane',
    lastName: 'Doe'
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        RouterTestingModule.withRoutes([]),
        MatSnackBarModule,
        MatCardModule,
        MatIconModule,
      ],
      declarations: [DetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => '1' } } }
        },
        {
          provide: SessionService,
          useValue: { sessionInformation: { id: 99, admin: true } }
        },
        { provide: FormBuilder, useValue: mockFb },
      ],
      schemas: [NO_ERRORS_SCHEMA] // Ignore les composants Angular Material et fxLayout pour simplifier
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should fetch session and teacher on init and render data', fakeAsync(() => {
    fixture.detectChanges();

    // On s'attend à une requête GET pour la session
    const reqSession = httpMock.expectOne(`api/session/1`);
    reqSession.flush(mockSession);

    // Puis une requête GET pour l'enseignant
    const reqTeacher = httpMock.expectOne(`api/teacher/42`);
    reqTeacher.flush(mockTeacher);

    tick();
    fixture.detectChanges();

    // Tests simples d’affichage du DOM :
    const compiled = fixture.nativeElement;

    expect(compiled.querySelector('h1').textContent).toContain('Test Session');
    expect(compiled.querySelector('mat-card-subtitle span').textContent).toContain('Jane DOE');
    expect(compiled.querySelector('.description').textContent).toContain('Description test');
    
    const divs = compiled.querySelectorAll('div[fxLayoutAlign="start center"]');

    const targetDiv = Array.from(divs).find(div => {
      const element = div as HTMLElement;
      const matIcon = element.querySelector('mat-icon');
      return matIcon?.textContent?.trim() === 'group';
    }) as HTMLElement | undefined;
    
    const attendeesSpan = targetDiv?.querySelector('span.ml1') as HTMLElement | undefined;

    expect(attendeesSpan).toBeTruthy();
    expect(attendeesSpan!.textContent).toContain('1 attendees');
  }));

  it('should call participate and refresh session on participate()', fakeAsync(() => {
    jest.spyOn(sessionApiService, 'participate').mockReturnValue(of(undefined));
    jest.spyOn(component as any, 'fetchSession')

    // Mock fetchSession HTTP requests
    fixture.detectChanges();

    // Réponse pour session et teacher
    httpMock.expectOne('api/session/1').flush(mockSession);
    httpMock.expectOne('api/teacher/42').flush(mockTeacher);

    component.participate();
    tick();

    const reqSession = httpMock.expectOne('api/session/1');
    expect(reqSession.request.method).toBe('GET');
    reqSession.flush(mockSession);
  
    const reqTeacher = httpMock.expectOne('api/teacher/42');
    expect(reqTeacher.request.method).toBe('GET');
    reqTeacher.flush(mockTeacher);
  
    expect(sessionApiService.participate).toHaveBeenCalledWith('1', '99');
    expect(component['fetchSession']).toHaveBeenCalledTimes(2); // ngOnInit + participate
  }));

  it('should call unParticipate and refresh session on unParticipate()', fakeAsync(() => {
    // Mock la méthode unParticipate pour qu'elle retourne un Observable vide
    jest.spyOn(sessionApiService, 'unParticipate').mockReturnValue(of(undefined));
  
    // Spy sur fetchSession pour vérifier qu'elle est appelée
    jest.spyOn(component as any, 'fetchSession');
  
    // Déclenche ngOnInit et les requêtes initiales
    fixture.detectChanges();
  
    // Répond aux requêtes initiales de ngOnInit (session + teacher)
    const reqSessionInit = httpMock.expectOne('api/session/1');
    expect(reqSessionInit.request.method).toBe('GET');
    reqSessionInit.flush(mockSession);
  
    const reqTeacherInit = httpMock.expectOne('api/teacher/42');
    expect(reqTeacherInit.request.method).toBe('GET');
    reqTeacherInit.flush(mockTeacher);
  
    // Appelle la méthode du composant
    component.unParticipate();
  
    tick();
  
    // Répond aux requêtes déclenchées par fetchSession appelée après unParticipate
    const reqSession = httpMock.expectOne('api/session/1');
    expect(reqSession.request.method).toBe('GET');
    reqSession.flush(mockSession);
  
    const reqTeacher = httpMock.expectOne('api/teacher/42');
    expect(reqTeacher.request.method).toBe('GET');
    reqTeacher.flush(mockTeacher);
  
    // Vérifie que fetchSession a bien été appelée
    expect((component as any).fetchSession).toHaveBeenCalled();
  }));
  
    it('should call window.history.back on back()', () => {
      jest.spyOn(window.history, 'back');
      component.back();
      expect(window.history.back).toHaveBeenCalled();
    });
});