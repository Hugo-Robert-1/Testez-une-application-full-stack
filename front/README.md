# Yoga

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

 ### 1. Se placer dans le dossier du projet :
  ```cd Testez-une-application-full-stack/front```
 ### 2. Installer les dépendances du projet :
  ``npm install ``

## Démarrer le projet
 Utiliser la commande : `` npm run start ``

## Pour lancer les tests unitaires et d'intégrations 
 Utiliser la commande : `` npm run test ``

## Génération d'un rapport de couverture via Jest 
 Utiliser la commande : ``npm run test -- --coverage ``

Un dossier coverage sera créé à la racine du projet : coverage/jest/lcov-report/app/index.html

## Pour lancer les tests e2e
 Utiliser la commande : `` npm run e2e ``

 La commande ``npm run cypress:open `` permet d'ouvrir l'interface de Cypress pour visionner les tests e2e dans un navigateur.

## Génération d'un rapport de couverture via Cypress 
 Pour générer le rapport de couverture, il faut s'assurer que l'application soit compilée avec du code couverture (instrumentation), pour cela il faut avoir au préalable lancer la commande ``npm run cypress:run`` qui va lancer les tests e2e et compiler les résultats dans le fichier out.json situé dans le dossier .nyc_output à la racine du projet. 
 Suite à cela, utiliser la commande : `` npm run e2e:coverage `` pour générer le rapport de couverture.

## Ressources

### Mockoon env 

### Postman collection

For Postman import the collection

> ressources/postman/yoga.postman_collection.json 

by following the documentation: 

https://learning.postman.com/docs/getting-started/importing-and-exporting-data/#importing-data-into-postman


### MySQL

SQL script for creating the schema is available `ressources/sql/script.sql`

By default the admin account is:
- login: yoga@studio.com
- password: test!1234