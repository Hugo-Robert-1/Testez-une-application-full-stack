describe('Login spec', () => {
  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('input[formControlName=password]').type("test!1234");

    cy.get('button[type="submit"]').should('not.be.disabled');
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/sessions')
  })

  it('Try to login with invalid credentials', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 401,
      body: { message: 'Invalid credentials' },
    });

    cy.get('input[formControlName=email]').type("user@email.com");
    cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`);

    cy.get('.error').should('be.visible').and('contain', 'An error occurred');
  });
});