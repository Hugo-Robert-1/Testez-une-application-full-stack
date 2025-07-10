import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;

  const mockUser : User = {
    id: 1,
    email: 'user@mail.com',
    lastName: 'lastname',
    firstName: 'firstname',
    admin: false,
    password: 'password',
    createdAt: new Date('2025-01-01T08:00:00'),
    updatedAt: new Date('2025-01-01T08:00:00')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [UserService]
    });

    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call GET on getById()', () => {
    const id = '1';

    service.getById(id).subscribe(result => {
      expect(result).toEqual(mockUser);
    });

    const req = httpMock.expectOne(`api/user/${id}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should call DELETE on delete()', () => {
    const id = '1';

    service.delete(id).subscribe(result => {
      expect(result).toBeNull();
    });

    const req = httpMock.expectOne(`api/user/${id}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});
