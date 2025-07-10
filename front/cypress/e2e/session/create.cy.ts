describe('Create Session spec', () => {
  it('shouldn\'t see create button if not admin', () => {
    // Custom method added to shortcut login process, can be found in support/commands.ts
    cy.loginNotAdmin('user@email.com', 'password');
    
    cy.get('button[routerlink="create"]').should('not.exist');
  });

  it('should see create button if admin and create a new session', () => {
    const mockTeachers = [
      { id: 1, firstName: 'Margot', lastName: 'DELAHAYE', createdAt: new Date(), updatedAt: new Date() },
      { id: 2, firstName: 'Hélène', lastName: 'THIERCELIN', createdAt: new Date(), updatedAt: new Date()}
    ]
    // Custom method added to shortcut login process, can be found in support/commands.ts
    cy.login('yoga@studio.com','test!1234');

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
  })
})