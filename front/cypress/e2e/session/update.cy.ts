describe('empty spec', () => {
  it('passes', () => {
    cy.login('yoga@studio.com', 'test!1234');

    cy.intercept('GET', '/api/session', [
      {
          id: 1,
          name: "Pilate",
          date: new Date(),
          teacher_id: 1,
          description: "Relax and energize.",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
      }
    ]).as('getSessions');

    cy.intercept('GET', '/api/session/1', {
      id: 1,
        name: "Pilate",
        date: new Date(),
        teacher_id: 1,
        description: "Relax and energize.",
        users: [],
        createdAt: new Date(),
        updatedAt: new Date()
    }).as('getSessionDetails');

    cy.intercept('PUT', '/api/session/1', {
        statusCode: 200
    });

    cy.get('button span').contains("Edit").click();

    cy.url().should('include', '/sessions/update/1');

    cy.get('input[formControlName=name]').clear();
    cy.get('input[formControlName=name]').type("Pilate");

    cy.get('button[type=submit]').click();

    cy.url().should('include', '/sessions');
  })
})