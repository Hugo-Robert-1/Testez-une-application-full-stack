describe('Delete session spec', () => {
  it('should delete a session if current user is admin', () => {
    cy.login('yoga@studio.com','test!1234');

    cy.intercept('DELETE', '/api/session/1', {
        statusCode: 200
    });
    
    cy.intercept('GET', '/api/session/1', {
          id: 1,
          name: 'Tennis',
          date: new Date(),
          teacher_id: 1,
          description: "Session detente.",
          users: [],
          createdAt: new Date(),
          updatedAt: new Date()
    }).as('getSession');

    cy.get('button span').contains("Detail").click();

    cy.url().should('include', '/sessions/detail/1');

    cy.get('button span').contains("Delete").click();

    cy.url().should('include', '/sessions');
  })
})