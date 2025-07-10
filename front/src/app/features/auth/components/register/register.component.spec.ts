import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { expect } from '@jest/globals';

import { RegisterComponent } from './register.component';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { of, throwError } from 'rxjs';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let newUser: any = {
    email: 'user@mail.com',
    firstName: 'firstname',
    lastName: 'lastname',
    password: 'Password51!'
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,  
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ]
    }).compileComponents();

    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create an account successfully if all fields are valid and then redirect to login page', () => {
    // Mock register() in order to simulate a valid registration
    jest.spyOn(authService, 'register').mockReturnValue(of(undefined));
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));

    // Fill the form with valid data and submit it
    component.form.controls['email'].setValue(newUser.email);
    component.form.controls['firstName'].setValue(newUser.firstName);
    component.form.controls['lastName'].setValue(newUser.lastName);
    component.form.controls['password'].setValue(newUser.password);
    component.submit();

    expect(authService.register).toHaveBeenCalledWith({
      email: newUser.email,
      firstName: newUser.firstName,
      lastName: newUser.lastName,
      password: newUser.password
    });

    expect(router.navigate).toHaveBeenCalledWith(['/login']);
  });

  it('should show an error message when a creation of a new user fail', () => {
    // Mock register method to throw an error when wrong registration happen
    jest.spyOn(authService, 'register').mockReturnValue(
      throwError(() => new Error('Email already exists'))
    );

    component.form.controls['email'].setValue(newUser.email);
    component.form.controls['firstName'].setValue(newUser.firstName);
    component.form.controls['lastName'].setValue(newUser.lastName);
    component.form.controls['password'].setValue(newUser.password);
    component.submit();
    
    fixture.detectChanges();

    // Retrieve the error message
    const errorMessage = fixture.nativeElement.querySelector('.error');
    expect(errorMessage).toBeTruthy(); 
    expect(errorMessage.textContent).toContain('An error occurred'); 
  });
});
