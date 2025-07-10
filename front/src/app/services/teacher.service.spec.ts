import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { TeacherService } from './teacher.service';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Teacher } from '../interfaces/teacher.interface';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  const mockTeacher : Teacher = {
    id: 1,
    firstName: 'firstname',
    lastName: 'lastname',
    createdAt: new Date('2025-01-01T08:00:00'),
    updatedAt: new Date('2025-01-01T08:00:00')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [TeacherService]
    });

    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should call GET on all()', () => {
    service.all().subscribe(result => {
      expect(result).toEqual([mockTeacher]);
    });

    const req = httpMock.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush([mockTeacher]);
  });

  it('should call GET on detail()', () => {
    const id = '1';

    service.detail(id).subscribe(result => {
      expect(result).toEqual(mockTeacher);
    });

    const req = httpMock.expectOne(`api/teacher/${id}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });
});
