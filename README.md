# 🧩 Dataset Generator — Architecture Microservices

## 📌 Présentation du projet

Ce projet est une **refonte microservices** d'une application de génération de datasets initialement monolithique.

L'objectif est de séparer la logique métier, d'améliorer les performances, et de rendre le système résilient face aux pannes réseau et aux charges importantes (ex : génération de millions de lignes).

---

## 🎯 Objectifs pédagogiques

Ce projet permet de mettre en pratique :

- ✔ **Architecture microservices**
- ✔ **Discovery Service** avec Eureka
- ✔ **API Gateway** avec Spring Cloud Gateway
- ✔ **Communication inter-services** avec OpenFeign
- ✔ **Résilience** avec Resilience4J
- ✔ **Conteneurisation** avec Docker / Docker Compose

---

## 🏗 Architecture globale

L'architecture est composée de **4 microservices indépendants** :

```
Client
  |
  v
API Gateway (8080)
  |
  +--> Dataset Manager Service (8081)
  |
  +--> Generator Service (8082)
  
Discovery Server (8761)
```

---

## 🔧 Description des services

### 1️⃣ Discovery Server (Eureka)

- **Rôle** : annuaire des services
- Permet la découverte dynamique des services
- Dashboard accessible via navigateur

**📍 Port** : `8761`

---

### 2️⃣ API Gateway

- Point d'entrée unique de l'application
- Route dynamiquement les requêtes vers les bons services
- Utilise les noms des services enregistrés dans Eureka

**📍 Port** : `8080`

#### Routes configurées

| Route | Destination |
|-------|-------------|
| `/DATASET-MANAGER/**` | dataset-manager-service |
| `/GENERATOR-SERVICE/**` | generator-service |

---

### 3️⃣ Dataset Manager Service

- Ancien monolithe allégé
- **Gère** :
  - Projets
  - Entités
  - Attributs
  - Base de données (H2)
- **Ne génère plus les datasets**
- Délègue la génération au Generator Service via Feign

**📍 Port** : `8081`

---

### 4️⃣ Generator Service

- Service **stateless**
- Responsable uniquement de la génération de datasets
- **Supporte** :
  - JSON
  - CSV
- Ne possède aucune base de données

**📍 Port** : `8082`

---

## 🔁 Communication inter-services

La communication entre le **Dataset Manager Service** et le **Generator Service** est réalisée via **OpenFeign**.

**Exemple :**

```java
@FeignClient(name = "GENERATOR-SERVICE")
public interface GeneratorClient {
    @PostMapping("/api/generate")
    byte[] generate(@RequestBody DatasetRequest request);
}
```

---

## 🛡 Résilience avec Resilience4J

Le Generator Service pouvant être lent ou indisponible, la communication est protégée par :

- ✔ **Circuit Breaker**
- ✔ **TimeLimiter**
- ✔ **Fallback contrôlé**

### Fallback retourné en cas de panne

```json
{
  "status": "PARTIAL",
  "message": "Service de génération momentanément indisponible",
  "data": []
}
```

⚠️ **Aucune stacktrace n'est exposée à l'utilisateur.**

---

## 🐳 Docker & Docker Compose

L'ensemble de l'écosystème peut être lancé avec **une seule commande** :

```bash
docker compose up --build
```

### Services exposés

| Service | Port |
|---------|------|
| Eureka | `8761` |
| API Gateway | `8080` |
| Dataset Manager | `8081` |
| Generator | `8082` |

---

## 🧪 Batterie de tests

### ✔ Vérification Eureka

```bash
curl -i http://localhost:8761
```

Les services suivants doivent être **UP** :
- `API-GATEWAY`
- `DATASET-MANAGER-SERVICE`
- `GENERATOR-SERVICE`

---

### ✔ Création d'un projet

```bash
curl -X POST http://localhost:8080/DATASET-MANAGER/api/projects \
  -H "Content-Type: application/json" \
  -d '{"name":"Project Demo","description":"demo"}'
```

---

### ✔ Création d'une entité

```bash
curl -X POST http://localhost:8080/DATASET-MANAGER/api/entities \
  -H "Content-Type: application/json" \
  -d '{"name":"Product","projectId":1}'
```

---

### ✔ Ajout d'un attribut

```bash
curl -X POST http://localhost:8080/DATASET-MANAGER/api/attributes \
  -H "Content-Type: application/json" \
  -d '{"name":"price","type":"DOUBLE","min":10,"max":20,"entityId":1}'
```

---

### ✔ Génération JSON

```bash
curl -s "http://localhost:8080/DATASET-MANAGER/api/export?projectId=1&format=json&count=3" | jq
```

---

### ✔ Génération CSV

```bash
curl -i "http://localhost:8080/DATASET-MANAGER/api/export?projectId=1&format=csv&count=3"
```

---

## 🧪 Test de résilience (obligatoire pour la soutenance)

### 1️⃣ Arrêter le Generator Service

```bash
docker compose stop generator-service
```

### 2️⃣ Nouvelle génération

```bash
curl -s "http://localhost:8080/DATASET-MANAGER/api/export?projectId=1&format=json&count=3" | jq
```

**Résultat attendu :**
- ✔ Le fallback est retourné
- ✔ Le Dataset Manager ne plante pas

---

### 3️⃣ Redémarrer le Generator Service

```bash
docker compose start generator-service
```

---

## ❌ Gestion des erreurs

### Projet inexistant

```bash
curl -i "http://localhost:8080/DATASET-MANAGER/api/export?projectId=9999&format=json"
```

**Résultat attendu :**
- ✔ HTTP 404
- ✔ Message clair

---

## 🧠 Choix techniques

- **Java 17**
- **Spring Boot 3**
- **Spring Cloud**
- **Eureka**
- **OpenFeign**
- **Resilience4J**
- **Docker / Docker Compose**
- **Gradle**

---

## ✅ Conclusion

Ce projet répond intégralement aux exigences du sujet :

- ✔ Microservices fonctionnels
- ✔ Gateway opérationnelle
- ✔ Découverte dynamique
- ✔ Résilience prouvée
- ✔ Démonstration prête pour la soutenance

---

## 💡 Recommandations pour la soutenance

### Optionnel mais fortement recommandé :

1. **Ajouter un schéma d'architecture** (draw.io ou Mermaid)
2. **Prévoir une vidéo de secours** de la démo
3. **Préparer le script oral** de présentation
4. **Justifier chaque choix technique** (pourquoi Eureka, pourquoi Resilience4J, etc.)

### Points clés à démontrer en direct :

- ✨ Consulter le dashboard Eureka pour voir les services enregistrés
- ✨ Effectuer une génération complète via l'API Gateway
- ✨ Arrêter le Generator Service et montrer le fallback
- ✨ Redémarrer le service et prouver que tout refonctionne

---

## 📂 Structure du projet

```
dataset-generator/
├── discovery-server/          # Eureka Server
├── api-gateway/              # Spring Cloud Gateway
├── dataset-manager-service/  # Gestion CRUD + Orchestration
├── generator-service/        # Génération de datasets
├── docker-compose.yml        # Orchestration des conteneurs
└── README.md                 # Ce fichier
```

---

## 🚀 Démarrage rapide

### Prérequis

- Docker & Docker Compose installés
- Ports 8080, 8081, 8082, 8761 disponibles

### Lancement

```bash
# Cloner le repository
git clone <repository-url>
cd dataset-generator

# Lancer tous les services
docker compose up --build

# Attendre que tous les services soient UP (30-60 secondes)

# Vérifier Eureka
curl http://localhost:8761
```

### Arrêt

```bash
docker compose down
```

---

## 📝 Licence

Ce projet est réalisé dans un cadre pédagogique.

---

## 👥 Auteur

[Votre nom]  
[Votre promotion]  
[Date de soutenance]

---

**Version** : 1.0  
**Dernière mise à jour** : Janvier 2025
