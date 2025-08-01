# Yoga App !

## Installation du projet 
 ### 1. Cloner le dépôt :
  ```git clone https://github.com/Hugo-Robert-1/Testez-une-application-full-stack.git ```
 ### 2. Se placer dans le dossier du projet :
  ```cd Testez-une-application-full-stack```
 ### 3. Installer les dépendances du projet :
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