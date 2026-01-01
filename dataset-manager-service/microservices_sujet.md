# Projet : Architecture Microservices — "Dataset"

## 1. Contexte et Objectifs

Votre application "Générateur de Datasets" fonctionne bien en version monolithique. Cependant, la génération de gros volumes de données (ex: 1 million de lignes) consomme beaucoup de ressources CPU et ralentit l'interface utilisateur et la gestion des projets.

**Votre mission :** Refondre l'architecture pour isoler la logique de génération dans un service dédié et sécuriser les échanges réseaux.

**Objectifs pédagogiques :**

- Mettre en place une **Discovery Service** (Eureka).

- Centraliser les accès via une **API Gateway**.

- Communiquer entre services via **OpenFeign**.

- Gérer les pannes réseaux avec **Resilience4J**.

## 2. Architecture Cible

Vous allez découper votre application en 4 composants distincts :

1. **Discovery Server** (Eureka) : L'annuaire des services.

2. **API Gateway** (Spring Cloud Gateway) : Le point d'entrée unique.

3. **Dataset Manager Service** (L'ancien monolithe allégé) : Gère les projets, les utilisateurs et la base de données (H2/Postgres).

4. **Generator Service** (Nouveau service) : Un service "ouvrier" (stateless) qui reçoit des règles de génération et renvoie les données générées.

----------

## 3. Étapes de réalisation

### Étape 1 : Infrastructure (Eureka & Gateway)

- Créer un projet Spring Boot pour le serveur **Eureka**.

- Créer un projet Spring Boot pour la **Gateway**.

- Configurer la Gateway pour router les requêtes de manière dynamique

  - `/DATASET-MANAGER/api/projects/**` -> vers `dataset-manager-service`

  - `/GENERATOR-SERVICE/api/generate/**` -> vers `generator-service`

### Étape 2 : Extraction du "Generator Service"

- Créer un nouveau microservice `generator-service`.

- Déplacer la logique de génération (les classes `DatasetGeneratorService`, les `Exporter`, et les logiques aléatoires) du monolithe vers ce nouveau service.

- **Endpoint** : Ce service doit exposer un endpoint POST (ex: `/generate`) qui :

  - Reçoit une définition de dataset (JSON contenant la structure : entités, champs, types, contraintes).

  - Renvoie le fichier ou les données générées (JSON/CSV).

- _Note : Ce service n'a pas besoin de base de données._

### Étape 3 : Communication via Feign

- Dans le `dataset-manager-service` (votre ancien monolithe), supprimez la logique de génération interne.

- Intégrez **OpenFeign**.

- Créez une interface `GeneratorClient` pour appeler le `generator-service`.

- Lorsque l'utilisateur clique sur "Générer", le Manager envoie la configuration au Generator et récupère le résultat.

### Étape 4 : Résilience avec Resilience4J

Le service de génération peut être lent ou indisponible. Vous devez protéger le `dataset-manager-service`.

- Implémentez un **Circuit Breaker** sur l'appel Feign.

- Définissez un **TimeLimiter** (ex: si la génération prend > x secondes, on coupe).

- Mettez en place un **Fallback** :

  - Si le générateur est HS ou trop lent, renvoyer une réponse par défaut à l'utilisateur (ex: un JSON contenant `{"status": "PARTIAL", "data": []}` ou un message "Service de génération momentanément indisponible, veuillez réessayer plus tard").

## 4. Scénario de démonstration attendu

Pour la soutenance/livraison, vous devrez démontrer le scénario suivant :

1. Lancer tous les services (Eureka, Gateway, Manager, Generator).

2. Via Postman (ou votre Front), créer un projet et demander une génération -> **Ça fonctionne (code 200).** -> visualisez le résultat brièvement.

3. **Couper le service `generator-service`** (l'arrêter brutalement).

4. Refaire une demande de génération depuis le Manager.

5. Le Manager ne doit pas planter (Stacktrace interdite). Il doit renvoyer le **Fallback** défini grâce à Resilience4J.

6. Montrer dans le dashboard Eureka que les services sont bien enregistrés.

:warning:  Lors de votre démonstration, votre scénario de démo doit être prêt, validé en amont, revalidé en amont pour ne pas rencontrer de problème le jour J, je vous encourage à prévoir une vidéo de secours au cas ou, c'est la seule chose qui pourra vous sauver en cas d'imprévu. Prévoyez un environnement calme, une connexion stable. :warning:

----------

## 5. Livrables et Contraintes

- **Repository Git** : Un repo contenant les dossiers des différents services.

- **Fichier `docker-compose.yml`** (Optionnel mais recommandé) pour lancer tout l'écosystème facilement.

- **Readme complet (MD)** : Expliquant les versions de Java / Spring Boot / Maven / Gradle, un schéma de l'architecture, la liste des endpoints REST,  la configuration du Circuit Breaker et les routes de la Gateway ainsi que tout ce que vous auriez pu rajouter.

**Conseil :** Ne perdez pas de temps sur le Frontend si vous n'avez pas tout terminé. Si votre Front existant fonctionnait avec le monolithe, il suffit de changer l'URL de base pour pointer vers la Gateway.

### Rendu

- Le rendu se fait exclusivement sur cet assignment : <https://classroom.github.com/a/_pcBz6SJ>
- Le dépôt ferme le 04/01/2026 à 23h59, si le projet n'est pas déposé, vous ne pourrez pas soutenir.
