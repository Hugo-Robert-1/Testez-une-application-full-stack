import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { expect } from '@jest/globals';
import { SessionApiService } from './session-api.service';
import { Session } from '../interfaces/session.interface';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  const mockSession: Session = {
    id: 1,
    name: 'Session 1',
    description: 'Description test',
    date: new Date('2025-01-01'),
    teacher_id: 42,
    users: [1, 2, 3],
    createdAt: new Date('2025-01-01T08:00:00'),
    updatedAt: new Date('2025-01-01T09:00:00')
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });

    service = TestBed.inject(SessionApiService);
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
      expect(result).toEqual([mockSession]);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush([mockSession]);
  });

  it('should call GET on detail()', () => {
    const id = '1';

    service.detail(id).subscribe(result => {
      expect(result).toEqual(mockSession);
    });

    const req = httpMock.expectOne(`api/session/${id}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should call DELETE on delete()', () => {
    const id = '1';

    service.delete(id).subscribe(result => {
      expect(result).toBeNull();
    });

    const req = httpMock.expectOne(`api/session/${id}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });

  it('should call POST on create()', () => {
    const newSession: Session = {
      name: 'New Session',
      description: 'Nouvelle session',
      date: new Date('2025-02-01'),
      teacher_id: 10,
      users: []
    };
    const returnedSession: Session = { ...newSession, id: 2 };

    service.create(newSession).subscribe(result => {
      expect(result).toEqual(returnedSession);
    });

    const req = httpMock.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newSession);
    req.flush(returnedSession);
  });

  it('should call PUT on update()', () => {
    const updatedSession: Session = {
      ...mockSession,
      name: 'Updated Session',
      description: 'Mise à jour'
    };

    service.update('1', updatedSession).subscribe(result => {
      expect(result).toEqual(updatedSession);
    });

    const req = httpMock.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  });

  it('should call POST on participate()', () => {
    const sessionId = '1';
    const userId = '100';

    service.participate(sessionId, userId).subscribe(result => {
      expect(result).toBeUndefined();
    });

    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toBeNull();
    req.flush(null);
  });

  it('should call DELETE on unParticipate()', () => {
    const sessionId = '1';
    const userId = '100';

    service.unParticipate(sessionId, userId).subscribe(result => {
      expect(result).toBeUndefined();
    });

    const req = httpMock.expectOne(`api/session/${sessionId}/participate/${userId}`);
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
  });
});