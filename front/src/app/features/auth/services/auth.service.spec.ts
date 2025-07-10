import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { AuthService } from './auth.service';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { expect } from '@jest/globals';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  const mockRegisterRequest: RegisterRequest = {
    email: 'user@email.com',
    firstName: 'firstname',
    lastName: 'lastname',
    password: 'password'
  }

  const mockLoginRequest: LoginRequest = {
    email: 'user@email.com',
    password: 'password'
  }

  const mockSessionInformation: SessionInformation = {
    token: 'token',
    type: 'type',
    id: 1,
    username: 'user',
    firstName: 'John',
    lastName: 'Smith',
    admin: false,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [AuthService],
    });

    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should call POST on register', () => {
    service.register(mockRegisterRequest).subscribe({
      next: () => {},
    });

    const req = httpMock.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(mockRegisterRequest);
    req.flush(null);
  })

  it('should call POST on login', () => {
    service.login(mockLoginRequest).subscribe((sessionInformation) => {
        expect(sessionInformation).toEqual(mockSessionInformation);
    });

      const req = httpMock.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(mockLoginRequest);

      req.flush(mockSessionInformation);
  })
});