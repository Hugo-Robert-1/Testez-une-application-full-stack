describe('Register page spec', () => {
  it('passes and submit button available only when all fields are filled', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: { message: 'User registered successfully!' }
    }).as('register');

    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('input[formControlName=email]').type("nouvelUser@email.com");
    cy.get('input[formControlName=firstName]').type("firstName");
    cy.get('input[formControlName=lastName]').type("lastName");
    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('input[formControlName=password]').type("azerty");

    cy.get('button[type="submit"]').should('not.be.disabled');
    cy.get('button[type="submit"]').click();

    cy.wait('@register').its('request.body').should('deep.equal', {
      email: 'nouvelUser@email.com',
      firstName: 'firstName',
      lastName: 'lastName',
      password: 'azerty',
    });

    cy.url().should('include', '/login');
  })

  it('should not allow to click on submit button if email is not in a valid format', () => {
    cy.visit('/register')

    cy.get('button[type="submit"]').should('be.disabled');
    cy.get('input[formControlName=firstName]').type("firstName");
    cy.get('input[formControlName=lastName]').type("lastName");
    cy.get('input[formControlName=password]').type("azerty");
    cy.get('button[type="submit"]').should('be.disabled');

    cy.get('input[formControlName=email]').type("nouvelUseremail.com");
    cy.get('button[type="submit"]').should('be.disabled');
  })
})