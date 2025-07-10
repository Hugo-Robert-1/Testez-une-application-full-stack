import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';

describe('RegisterComponent Integration Test', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let httpMock: HttpTestingController;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatInputModule,
        MatButtonModule,
        HttpClientTestingModule,
        NoopAnimationsModule,
      ],
      providers: [
        AuthService,
        {
          provide: Router,
          useValue: { navigate: jest.fn() },
        },
      ],
    }).compileComponents();
  

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty values', () => {
    expect(component.form.value).toEqual({
      email: '',
      firstName: '',
      lastName: '',
      password: '',
    });
  });

  it('should require that all fields are filled to be valid', () => {
    component.form.setValue({
      email: '',
      firstName: '',
      lastName: '',
      password: '',
    });

    expect(component.form.valid).toBeFalsy();

    component.form.setValue({
      email: '',
      firstName: 'John',
      lastName: 'Smith',
      password: 'password',
    });

    expect(component.form.valid).toBeFalsy();

    component.form.setValue({
      email: 'user@email.com',
      firstName: 'John',
      lastName: 'Smith',
      password: 'password',
    });

    expect(component.form.valid).toBeTruthy();
  });

  it('should call /register and move to /login if successful registration', fakeAsync(() => {
    component.form.setValue({
      email: 'user@email.com',
      firstName: 'John',
      lastName: 'Smith',
      password: 'password',
    });

    component.submit();

    // Intercept the HTTP request
    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({
      email: 'user@email.com',
      firstName: 'John',
      lastName: 'Smith',
      password: 'password',
    });

    req.flush({});

    tick();

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  }));

  it('should get an Error when registration fails', fakeAsync(() => {
    component.form.setValue({
      email: 'user@email.com',
      firstName: 'John',
      lastName: 'Smith',
      password: 'password',
    });

    component.submit();

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');

    req.flush('Error', { status: 400, statusText: 'Bad Request' });

    tick();

    expect(component.onError).toBeTruthy();
  }));
});