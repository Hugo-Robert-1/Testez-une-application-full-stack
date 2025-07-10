describe('Not Found Page spec', () => {
  it('should display a Page not found and a 404 in url', () => {
    cy.visit('/notfound')

    cy.contains('Page not found !')
    cy.url().should('include', '/404')
  })
})