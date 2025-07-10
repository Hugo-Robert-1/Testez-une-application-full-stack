describe('Me page test', () => {
  it('should show the current user information', () => {
    let sessionUsers: Number[] = [];

    // Custom method added to shortcut login process, can be found in support/commands.ts
    cy.login('yoga@studio.com','test!1234');

    cy.intercept(
      {
        method: 'GET',
        url: '/api/user/1',
      },
      {
        id: 1,
        email: "yoga@studio.com",
        lastName: 'AdminLN',
        firstName: 'Admin',
        admin: false,
        createdAt: new Date(),
        updatedAt: new Date()
      },
    ).as('user')

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
          description: "A relaxing yoga session",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
        }
      ]).as('session')


    cy.get('span[routerLink=me]').click().then(()=>{
      cy.url().should('include', '/me').then(()=>{
        cy.get('p').contains("Name: Admin "+ ("AdminLN").toUpperCase())
        cy.get('p').contains("Email: yoga@studio.com")
      })
    })

    cy.get('button').first().click()
    cy.url().should('include', '/sessions')
  })

  it('should delete the user account', () => {
    // Custom method added to shortcut login process, can be found in support/commands.ts
    cy.login('yoga@studio.com', 'test!1234');

    cy.intercept('GET', '/api/user/1', {
      id: 1,
      email: "johnSmith@email.com",
      lastName: 'Smith',
      firstName: 'John',
      admin: false,
      createdAt: new Date(),
      updatedAt: new Date()
    }).as('user');

    cy.intercept('DELETE', '/api/user/1', {
      statusCode: 200,
    }).as('deleteUser');

    cy.get('span[routerLink=me]').click();
    cy.url().should('include', '/me');

    cy.get('button').contains('Detail').click();
    cy.wait('@deleteUser').its('request.method').should('eq', 'DELETE');

    cy.get('.mat-snack-bar-container').should('contain.text', 'Your account has been deleted !');

    cy.url().should('eq', 'http://localhost:4200/');
  });
});