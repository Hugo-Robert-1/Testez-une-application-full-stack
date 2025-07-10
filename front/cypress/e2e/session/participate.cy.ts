describe('User participation spec', () => {

  it('Login successfully and go to a existing session', () => {
      cy.loginNotAdmin('user@email.com','password');

      // Intercept API call for fetching a specific session
      cy.intercept(
          {
              method: 'GET',
              url: '/api/session/1',
          },
          {
              id: 1,
              name: "Test",
              date: new Date(),
              teacher_id: 1,
              description: "Test description",
              users: [],
              createdAt: new Date(),
              updatedAt: new Date()
          }
      ).as('session');

      cy.get('button span').contains("Detail").click();
      cy.url().should('include', '/sessions/detail/1');
  });

  it('Participate in a session', () => {
      let sessionUsers: Number[] = [1];

      cy.intercept('GET', '/api/teacher/1', {
          body: { id: 1, firstName: 'Margot', lastName: 'DELAHAYE', createdAt: new Date(), updatedAt: new Date() },
      });

      cy.intercept('POST', '/api/session/1/participate/1', {
          status: 200,
      });

      cy.intercept(
          {
              method: 'GET',
              url: '/api/session/1',
          },
          {
              id: 1,
              name: "Test",
              date: new Date(),
              teacher_id: 1,
              description: "Test description",
              users: sessionUsers,
              createdAt: new Date(),
              updatedAt: new Date()
          }
      ).as('session');

      // Participate in session and verify changes
      cy.get('h1').contains("Test").then(() => {
          sessionUsers.push(1);
          cy.get('button span').contains("Participate").click().then(() => {
              cy.wait(500);
              cy.get('button span').contains('Do not participate');
              cy.get('span[class=ml1]').contains("1 attendees");
          });
      });
  });

  it('Stop participating in a session', () => {
      cy.intercept('GET', '/api/teacher', {
        body: [
          { id: 1, firstName: 'Margot', lastName: 'DELAHAYE', createdAt: new Date(), updatedAt: new Date() },
          { id: 2, firstName: 'Hélène', lastName: 'THIERCELIN', createdAt: new Date(), updatedAt: new Date()}
        ]
      });

      cy.intercept('DELETE', '/api/session/1/participate/1', {
          status: 200,
      });

      cy.intercept(
          {
              method: 'GET',
              url: '/api/session',
          },
          []
      ).as('session');

      cy.intercept(
          {
              method: 'GET',
              url: '/api/session/1',
          },
          {
              id: 1,
              name: "Test",
              date: new Date(),
              teacher_id: 1,
              description: "Test description",
              users: [],
              createdAt: new Date(),
              updatedAt: new Date()
          }
      ).as('session');

      // Opt out of session and verify changes
      cy.get('button span').contains("Do not participate").click();
      cy.get('span[class=ml1]').contains("0 attendees");
  });

  it('Logout successfully', () => {
      // Click the logout link
      cy.get('span[class=link]').contains("Logout").click();

      // Verify redirection to the homepage
      cy.url().should('eq', 'http://localhost:4200/');
  });
});