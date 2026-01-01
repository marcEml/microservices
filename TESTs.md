# Guide de Tests - Architecture Microservices

Ce guide présente une batterie complète de tests pour valider l'architecture microservices incluant Eureka, API Gateway, Dataset-Manager, Generator-Service et Resilience4J.

## 📋 Prérequis

### Architecture des Services

- **Eureka Server** : `http://localhost:8761`
- **API Gateway** : `http://localhost:8080`
- **Dataset-Manager** : accessible via `/DATASET-MANAGER/...`
- **Generator-Service** : accessible via `/GENERATOR-SERVICE/...`

### Outils Nécessaires

- `curl` pour les requêtes HTTP
- `jq` pour le parsing JSON (optionnel mais recommandé)
- Docker Compose pour la gestion des conteneurs

---

## 🔍 0. Vérification Initiale

### Eureka Server

```bash
curl -s http://localhost:8761 | head
```

### Health Checks (si Actuator activé)

```bash
# Gateway
curl -i http://localhost:8080/actuator/health

# Dataset-Manager
curl -i http://localhost:8081/actuator/health

# Generator-Service
curl -i http://localhost:8082/actuator/health
```

---

## 🌐 A. Tests via API Gateway

### 1. Lister les Projets

```bash
curl -i "http://localhost:8080/DATASET-MANAGER/api/projects"
```

### 2. Créer un Projet

```bash
curl -s -X POST "http://localhost:8080/DATASET-MANAGER/api/projects" \
  -H "Content-Type: application/json" \
  -d '{"name":"Project Demo","description":"demo"}'
```

**Avec récupération automatique de l'ID :**

```bash
PROJECT_ID=$(curl -s -X POST "http://localhost:8080/DATASET-MANAGER/api/projects" \
  -H "Content-Type: application/json" \
  -d '{"name":"Project Demo","description":"demo"}' | jq -r .id)

echo "PROJECT_ID=$PROJECT_ID"
```

### 3. Créer une Entité (Product)

```bash
ENTITY_ID=$(curl -s -X POST "http://localhost:8080/DATASET-MANAGER/api/entities" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Product\",\"projectId\":$PROJECT_ID}" | jq -r .id)

echo "ENTITY_ID=$ENTITY_ID"
```

### 4. Ajouter un Attribut

Création d'un attribut `price` de type `DOUBLE` avec contraintes min/max :

```bash
ATTR_ID=$(curl -s -X POST "http://localhost:8080/DATASET-MANAGER/api/attributes" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"price\",\"type\":\"DOUBLE\",\"min\":10,\"max\":20,\"entityId\":$ENTITY_ID}" | jq -r .id)

echo "ATTR_ID=$ATTR_ID"
```

### 5. Vérifier l'Entité

```bash
curl -s "http://localhost:8080/DATASET-MANAGER/api/entities/$ENTITY_ID" | jq
```

### 6. Export JSON (Flow Complet)

Test du flux complet : Manager → Feign → Generator

```bash
curl -i "http://localhost:8080/DATASET-MANAGER/api/export?projectId=$PROJECT_ID&format=json&count=3"
```

**Version lisible avec jq :**

```bash
curl -s "http://localhost:8080/DATASET-MANAGER/api/export?projectId=$PROJECT_ID&format=json&count=3" | jq
```

✅ **Résultat attendu** : JSON contenant `projectName` et `records` avec des prix entre 10 et 20.

### 7. Export CSV

```bash
curl -i "http://localhost:8080/DATASET-MANAGER/api/export?projectId=$PROJECT_ID&format=csv&count=3"
```

---

## 🔧 B. Tests Directs du Generator-Service

### 8. POST Direct au Generator via Gateway

Test du Generator-Service de manière isolée :

```bash
curl -i -X POST "http://localhost:8080/GENERATOR-SERVICE/api/generate" \
  -H "Content-Type: application/json" \
  -d "{
    \"count\": 3,
    \"format\": \"json\",
    \"dataset\": {
      \"projectName\": \"Demo Direct\",
      \"entities\": [
        {
          \"name\": \"Product\",
          \"attributes\": [
            {\"name\":\"price\",\"type\":\"DOUBLE\",\"min\":10,\"max\":20}
          ]
        }
      ]
    }
  }"
```

✅ **Résultat attendu** : JSON avec 3 enregistrements générés.

---

## 🛡️ C. Tests Resilience4J / Fallback

### 9. Arrêter le Generator-Service

Dans un terminal séparé :

```bash
docker compose stop generator-service
```

### 10. Tester le Fallback

Effectuer un export alors que le Generator-Service est arrêté :

```bash
curl -i "http://localhost:8080/DATASET-MANAGER/api/export?projectId=$PROJECT_ID&format=json&count=3"
```

✅ **Résultat attendu** : 200 OK (ou 503 selon configuration) avec :

```json
{
  "status": "PARTIAL",
  "message": "Service de génération momentanément indisponible",
  "data": []
}
```

### 11. Redémarrer le Generator-Service

```bash
docker compose start generator-service
```

### 12. Vérifier le Retour à la Normale

```bash
curl -s "http://localhost:8080/DATASET-MANAGER/api/export?projectId=$PROJECT_ID&format=json&count=3" | jq
```

---

## ⚠️ D. Tests de Robustesse

### 13. Projet Inexistant

Test de gestion d'erreur 404 :

```bash
curl -i "http://localhost:8080/DATASET-MANAGER/api/export?projectId=999999&format=json&count=3"
```

✅ **Résultat attendu** : 404 Not Found avec message d'erreur approprié.

### 14. Format Non Supporté

```bash
curl -i "http://localhost:8080/DATASET-MANAGER/api/export?projectId=$PROJECT_ID&format=xml&count=3"
```

✅ **Résultat attendu** : 400 Bad Request (idéal) ou gestion d'erreur appropriée.

---

## 🚀 E. Script d'Automatisation Complet

Script bash pour exécuter l'ensemble du parcours automatiquement :

```bash
#!/bin/bash
set -e

BASE="http://localhost:8080/DATASET-MANAGER"

echo "=== Création du projet ==="
PROJECT_ID=$(curl -s -X POST "$BASE/api/projects" \
  -H "Content-Type: application/json" \
  -d '{"name":"Project Demo","description":"demo"}' | jq -r .id)
echo "PROJECT_ID=$PROJECT_ID"

echo "=== Création de l'entité ==="
ENTITY_ID=$(curl -s -X POST "$BASE/api/entities" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Product\",\"projectId\":$PROJECT_ID}" | jq -r .id)
echo "ENTITY_ID=$ENTITY_ID"

echo "=== Ajout de l'attribut ==="
curl -s -X POST "$BASE/api/attributes" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"price\",\"type\":\"DOUBLE\",\"min\":10,\"max\":20,\"entityId\":$ENTITY_ID}" | jq

echo "=== EXPORT JSON ==="
curl -s "$BASE/api/export?projectId=$PROJECT_ID&format=json&count=3" | jq

echo "=== EXPORT CSV ==="
curl -s "$BASE/api/export?projectId=$PROJECT_ID&format=csv&count=3"
echo
```

### Utilisation du Script

1. Sauvegarder le script dans un fichier (ex: `test-microservices.sh`)
2. Rendre le script exécutable : `chmod +x test-microservices.sh`
3. Exécuter : `./test-microservices.sh`

---

## 📊 Checklist de Validation

- [ ] Tous les services sont enregistrés dans Eureka
- [ ] L'API Gateway route correctement vers Dataset-Manager
- [ ] L'API Gateway route correctement vers Generator-Service
- [ ] Le CRUD complet fonctionne (Project, Entity, Attribute)
- [ ] L'export JSON génère des données valides
- [ ] L'export CSV génère des données valides
- [ ] Le fallback Resilience4J s'active correctement
- [ ] Les erreurs 404 sont gérées proprement
- [ ] Le système se rétablit après redémarrage du service

---

## 🔧 Configuration Recommandée

### Pour une Soutenance Professionnelle

Il est recommandé de configurer le fallback pour retourner :
- **Code HTTP 503** (Service Unavailable) plutôt que 200
- Un message clair indiquant l'indisponibilité temporaire
- Une structure JSON cohérente avec les réponses normales

Exemple de réponse fallback idéale :

```json
{
  "status": "SERVICE_UNAVAILABLE",
  "message": "Le service de génération est temporairement indisponible. Veuillez réessayer dans quelques instants.",
  "timestamp": "2025-01-01T12:00:00Z",
  "data": null
}
```

---

## 📝 Notes

- Tous les tests supposent que Docker Compose est configuré et que les services sont démarrés
- Les ports par défaut peuvent être modifiés selon votre configuration
- Pour une démo en soutenance, préparez quelques projets de test à l'avance
- Testez le fallback plusieurs fois pour montrer la résilience du système

---

## 🐛 Dépannage

### Le service ne répond pas
```bash
docker compose logs -f [service-name]
```

### Eureka ne voit pas les services
- Vérifier les fichiers `application.yml` de chaque service
- S'assurer que `eureka.client.service-url.defaultZone` est correct

### Erreur de connexion Feign
- Vérifier que les noms de services dans `@FeignClient` correspondent aux noms dans Eureka
- Vérifier les logs du Gateway et du Dataset-Manager

---

**Version**: 1.0  
**Dernière mise à jour**: Janvier 2025
