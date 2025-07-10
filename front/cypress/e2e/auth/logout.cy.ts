describe('Logout spec', () => {
  it('Logout successfull', () => {
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

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/sessions')

    cy.get('span[class=link]').contains("Logout").click();

    cy.url().should('eq', 'http://localhost:4200/');
  })
})