import { fakeAsync, TestBed, tick } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;
  const sessionInfo : SessionInformation = {
      token: 'token',
      type: 'Bearer',
      id: 1,
      username: 'username',
      firstName: 'firstname',
      lastName: 'lastName',
      admin: false
  };

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('logIn', () => {
    it('should set isLogged to true and update sessionInformation', () => {
      service.logIn(sessionInfo);

      expect(service.isLogged).toBe(true);
      expect(service.sessionInformation).toEqual(sessionInfo);
      expect(service.$isLogged()).toBeTruthy();
      
    });

    it('should be false initially', (done) => {
      service.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBe(false);
        done();
      });
    });

    it('should be true after login', (done) => {
      service.logIn(sessionInfo);
      service.$isLogged().subscribe(isLogged => {
        expect(isLogged).toBe(true);
        done();
      });
    });

    it('should emit false then true when logging in', (done) => {
      const emitted: boolean[] = [];
      const subscription = service.$isLogged().subscribe(value => {
        emitted.push(value);
        // On attend deux valeurs : false (avant login), puis true (après login)
        if (emitted.length === 2) {
          expect(emitted).toEqual([false, true]);
          subscription.unsubscribe();
          done();
        }
      });

      service.logIn(sessionInfo);
    });
  });

  describe('logOut', () => {
    it('should set isLogged to false and clear sessionInfo', () => {
      service.logIn(sessionInfo);
      expect(service.isLogged).toBe(true);
      expect(service.sessionInformation).toEqual(sessionInfo);
      
      service.logOut();

      expect(service.isLogged).toBe(false);
      expect(service.sessionInformation).toBeUndefined();
    });

    it('should emit false when logging out', fakeAsync(() => {
      const emitted: boolean[] = [];
      const subscription = service.$isLogged().subscribe(value => {
        emitted.push(value);
      });

      service.logIn(sessionInfo); // => true
      tick(); // force la propagation de l’émission

      service.logOut(); // => false
      tick(); // force encore la propagation

      expect(emitted).toEqual([false, true, false]); // la première valeur par défaut est false
        subscription.unsubscribe();
      }));
  });
});
