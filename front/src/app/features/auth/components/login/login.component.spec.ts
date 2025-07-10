import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { of } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [SessionService],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();

    authService = TestBed.inject(AuthService);
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should disable the submit button if the form is empty', () => {
    const form = component.form;
    expect(form.valid).toBeFalsy();

    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');

    // Verify that the disabled property of the submit button is at true when the form is empty
    expect(submitButton.disabled).toBeTruthy(); 
  });

  it('should disable the submit button if the form is not fully filled', () => {
    component.form.controls['email'].setValue('test@example.com');

    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');

    // Verify that the disabled property of the submit button is true when one property is missing
    expect(submitButton.disabled).toBeTruthy(); 
  });

  it('should log in successfully with valid credentials', () => {
      jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));

      const mockSessionInformation: SessionInformation = {
        token: 'token',
        type: 'Bearer',
        id: 1,
        username: 'username',
        firstName: 'firsname',
        lastName: 'lastname',
        admin: false
      };

      jest.spyOn(authService, 'login').mockReturnValue(of(mockSessionInformation)); 
      jest.spyOn(sessionService, 'logIn');

      component.form.controls['email'].setValue('user@mail.com');
      component.form.controls['password'].setValue('password51!');

      fixture.detectChanges();
      const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
      expect(submitButton.disabled).toBeFalsy(); 

      component.submit();

      expect(authService.login).toHaveBeenCalledWith({
        email: 'user@mail.com',
        password: 'password51!'
      });
      expect(sessionService.logIn).toHaveBeenCalledWith(mockSessionInformation);
      expect(router.navigate).toHaveBeenCalledWith(['/sessions']);
  });
});
