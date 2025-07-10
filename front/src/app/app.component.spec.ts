import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';
import { of } from 'rxjs';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';

describe('AppComponent', () => {
  let sessionServiceMock: any;

  beforeEach(async () => {
    // Mock Session service with the methods needed for the tests
    sessionServiceMock = {
      $isLogged: jest.fn().mockReturnValue(of(true)),
      logOut: jest.fn()
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
      providers: [
        { 
          provide: SessionService,
          useValue: sessionServiceMock
        } 
      ]
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it('should display 3 links in the toolbar when connected', () => {
    sessionServiceMock.$isLogged.mockReturnValue(of(true));

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const links = compiled.querySelectorAll('.link');
    
    expect(links.length).toBe(3);
    expect(links[0].textContent).toContain('Sessions');
    expect(links[1].textContent).toContain('Account');
    expect(links[2].textContent).toContain('Logout');
  });

  it('should display 2 links in the toolbar when not connected', () => {
    sessionServiceMock.$isLogged.mockReturnValue(of(false));

    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const links = compiled.querySelectorAll('.link');
    
    expect(links.length).toBe(2);
    expect(links[0].textContent).toContain('Login');
    expect(links[1].textContent).toContain('Register');
  });

  it('should call logOut and navigate to home on logout', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;

    // Verify the route navigation
    const router = TestBed.inject(Router);
    const navigateSpy = jest.spyOn(router, 'navigate');

    app.logout();

    expect(navigateSpy).toHaveBeenCalledWith(['']);
    expect(sessionServiceMock.logOut).toHaveBeenCalled();
  });
});
