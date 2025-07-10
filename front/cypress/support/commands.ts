// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
// declare namespace Cypress {
//   interface Chainable<Subject = any> {
//     customCommand(param: any): typeof customCommand;
//   }
// }
//
// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })

// Ajoute la fonction login aux méthodes utilisables via Cypress pour simplifier la connexion dans les tests e2e
Cypress.Commands.add('login', (email: string, password: string) => {
  cy.visit('/login')

  cy.intercept('POST', '/api/auth/login', {
    body: {
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: true
    },
  }).as("login");

  cy.intercept(
    {
      method: 'GET',
      url: '/api/session',
    },
    [
      {
        id: 1,
        name: 'Session 1',
        date: new Date(),
        teacher_id: 1,
        description: "Description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ]
  ).as('session')

  cy.get('input[formControlName=email]').type(email)
  cy.get('input[formControlName=password]').type(password)
  cy.get('button[type="submit"]').click();

  cy.url().should('include', '/sessions')
});

Cypress.Commands.add('loginNotAdmin', (email: string, password: string) => {
  cy.visit('/login')

  cy.intercept('POST', '/api/auth/login', {
    body: {
      id: 1,
      username: 'userName',
      firstName: 'firstName',
      lastName: 'lastName',
      admin: false
    },
  }).as("login");

  cy.intercept(
    {
      method: 'GET',
      url: '/api/session',
    },
    [
      {
        id: 1,
        name: 'Session 1',
        date: new Date(),
        teacher_id: 1,
        description: "Description",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ]
  ).as('session')

  cy.get('input[formControlName=email]').type(email)
  cy.get('input[formControlName=password]').type(password)
  cy.get('button[type="submit"]').click();

  cy.url().should('include', '/sessions')
});

Cypress.Commands.add('createSession', () => {
  const mockTeachers = [
      { id: 1, firstName: 'Margot', lastName: 'DELAHAYE', createdAt: new Date(), updatedAt: new Date() },
      { id: 2, firstName: 'Hélène', lastName: 'THIERCELIN', createdAt: new Date(), updatedAt: new Date()}
   ];

    cy.intercept('GET', '/api/teacher', {
        body: mockTeachers
    });

    cy.intercept('POST', '/api/session', {
        statusCode: 200
    });

    cy.get('button[routerlink="create"]').should('exist').click();

    cy.url().should('include', '/sessions/create');

    // Fill the form
    cy.get('input[formControlName=name]').type("Nouvelle Session");
    cy.get('input[formControlName=date]').type("2023-10-10");

    // Pick the teacher from the dropdown
    cy.get('mat-select[formControlName=teacher_id]').click().then(() => {
      cy.get('.cdk-overlay-container .mat-select-panel .mat-option-text')
        .should('contain', mockTeachers[0].firstName);
      cy.get(`.cdk-overlay-container .mat-select-panel .mat-option-text:contains(${mockTeachers[0].firstName})`)
        .first()
        .click()
        .then(() => {
          cy.get(`[formcontrolname=teacher_id]`).contains(mockTeachers[0].firstName);
        });
    });
    cy.get('textarea[formControlName=description]').type("Nouvellesession.");

    cy.get('button[type=submit]').click();
    cy.url().should('include', '/sessions');
});