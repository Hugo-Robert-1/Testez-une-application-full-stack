import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';

import { MeComponent } from './me.component';
import { Router } from '@angular/router';
import { UserService } from 'src/app/services/user.service';
import { of } from 'rxjs';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let router: any;
  let matSnackBar: any;
  let userService: UserService;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut: jest.fn()
  };

  beforeEach(async () => {
    const mockUserService = {
      getById: jest.fn().mockReturnValue(of({
        id: 1,
        email: 'john.doe@test.com',
        lastName: 'Doe',
        firstName: 'John',
        admin: true,
        password: 'password',
        createdAt: new Date(),
        updatedAt: new Date()
      })),
      delete: jest.fn().mockReturnValue(of({}))
    };

    const mockMatSnackBar = { open: jest.fn() };
    const mockRouter = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: Router, useValue: mockRouter }
      ],
    }).compileComponents();

    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);
    userService = TestBed.inject(UserService);
    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should not show delete button if user is admin', () => {
    const deleteButton = fixture.nativeElement.querySelector('button.delete');
    expect(deleteButton).toBeNull();
  })

  it('should delete the user, show a notification, log out, and redirect to home', () => {
    const deleteSpy = jest.spyOn(userService, 'delete').mockReturnValue(of({}));
    const snackBarSpy = jest.spyOn(matSnackBar, 'open');
    const logOutSpy = jest.spyOn(mockSessionService, 'logOut'); 
    const navigateSpy = jest.spyOn(router, 'navigate');

    component.delete();

    expect(deleteSpy).toHaveBeenCalledWith(mockSessionService.sessionInformation.id.toString());
    expect(snackBarSpy).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
    expect(logOutSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/']); 
  });
});
