# Yoga App !

Ce repository regroupe la partie backend et la partie frontend nécessaire au fonctionnement de l'application

## Installation du projet
### Cloner le dépôt :
  ```git clone https://github.com/Hugo-Robert-1/Testez-une-application-full-stack.git ```

# Back-end
 ### 1. Se placer dans le dossier du projet :
  ```cd Testez-une-application-full-stack/back```
 ### 2. Installer les dépendances du projet :
  ``mvn clean install ``

## Installer une base de données MySQL en local
 1. **Installer MySQL** : Assurez-vous que MySQL est installé sur votre machine. Si ce n'est pas le cas, vous pouvez le télécharger et l'installer via [MySQL Downloads](https://dev.mysql.com/downloads/) en suivant le programme d'installation.

 2. **Créer la base de données** : Connectez-vous à MySQL et créez la base de données `test`. Le fichier de configuration pour se connecter le backend à la base de données a déjà des valeurs en place, libre à vous de les modifier pour les adapter à votre environement personnel (Le fichier se trouve à l'adresse suivant : Testez-une-application-full-stack/back/src/main/resources/application.properties).

    ```sql
    CREATE DATABASE test;
    ```
 
 3. **Mise en place des tables de la base de données** : Pour ce projet, les tables seront créées automatiquement au démarage de l'application 

## Démarrer le projet
 Utiliser la commande : `` mvn spring-boot:run ``

## Pour lancer les tests 
 Utiliser la commande : `` mvn clean test ``

## Génération d'un rapport de couverture via Jacoco 
 Utiliser la commande : `` mvn clean test jacoco:report ``

 Il est aussi possible pour ce projet d'effectuer un rapport de couverture au démarrage via la commande : `` mvn clean package `` 

 Le rapport de couverture sera situé à l'adresse suivante : target/site/jacoco/index.html

# Front-end
 ### 1. Se placer dans le dossier du projet :
  ```cd Testez-une-application-full-stack/front```
 ### 2. Installer les dépendances du projet :
  ``npm install ``

## Démarrer le projet
 Utiliser la commande : `` npm run start ``

## Pour lancer les tests 
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