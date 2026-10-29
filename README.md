# TP : Service Web RESTful avec Spring Boot, Jersey (JAX-RS), JPA et H2

Ce projet est une implémentation personnalisée et enrichie du TP sur les **Services Web RESTful** utilisant le framework standard **JAX-RS (via Jersey)** intégré à **Spring Boot 3**.

---

## 📁 Structure du Projet

Conforme à l'architecture requise :

```
jaxrs/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ma/ws/jaxrs/
│   │   │       ├── config/
│   │   │       │   └── MyConfig.java               # Enregistrement Jersey ResourceConfig
│   │   │       ├── controllers/
│   │   │       │   └── CompteRestJaxRSAPI.java     # Endpoints REST JAX-RS (JSON & XML)
│   │   │       ├── entities/
│   │   │       │   ├── Compte.java                 # Entité JPA + JAXB @XmlRootElement
│   │   │       │   └── TypeCompte.java             # Enumération (COURANT, EPARGNE)
│   │   │       ├── repositories/
│   │   │       │   └── CompteRepository.java       # Interface Spring Data JPA
│   │   │       └── JaxrsApplication.java           # Point d'entrée & CommandLineRunner
│   │   └── resources/
│   │       └── application.properties              # Configuration Port 8082, H2 in-memory
```

---

## 🚀 Personnalisations Apportées (Points Forts pour le Rapport de TP)

1. **Modèle de données enrichi** :
   - Ajout d'un champ `titulaire` (nom du propriétaire) et `devise` (ex: "DH").
   - Utilisation de `EnumType.STRING` pour des affichages clairs en JSON et XML (au lieu de 0/1).
   - Annotations XML JAXB complètes (`@XmlRootElement`, `@XmlAccessorType(XmlAccessType.FIELD)`).

2. **Endpoints métiers personnalisés (au-delà du CRUD de base)** :
   - `GET /banque/comptes/type/{type}` : Filtrage direct par type de compte.
   - `PUT /banque/comptes/{id}/verser?montant=...` : Opération de crédit sur un compte.
   - `PUT /banque/comptes/{id}/retirer?montant=...` : Opération de débit avec vérification de solde.
   - `GET /banque/comptes/stats/solde-total` : Tableau de bord statistiques (solde total, répartition).

3. **Compatibilité Spring Boot 3 / Jakarta EE** :
   - Migration complète vers `jakarta.*` (évite les conflits `javax` vs `jakarta` fréquents sur Spring Boot 3).
   - Intégration transparente de la sérialisation bi-format (JSON et XML) via l'en-tête `Accept`.

4. **Console H2 & Données Réalistes** :
   - Console activée sur `http://localhost:8082/h2-console` (JDBC URL: `jdbc:h2:mem:banque`).
   - Initialisation avec des données de comptes réels au démarrage.

---

## 🧪 Guide de Test des Endpoints (cURL, Postman, SoapUI)

### 1. Récupérer tous les comptes (JSON)
```bash
curl -X GET "http://localhost:8082/banque/comptes" -H "Accept: application/json"
```

### 2. Récupérer tous les comptes (XML)
```bash
curl -X GET "http://localhost:8082/banque/comptes" -H "Accept: application/xml"
```

### 3. Récupérer un compte par ID
```bash
# En JSON
curl -X GET "http://localhost:8082/banque/comptes/1" -H "Accept: application/json"

# En XML
curl -X GET "http://localhost:8082/banque/comptes/1" -H "Accept: application/xml"
```

### 4. Ajouter un nouveau compte (POST)
**En JSON :**
```bash
curl -X POST "http://localhost:8082/banque/comptes" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d "{\"solde\": 8500.0, \"type\": \"EPARGNE\", \"titulaire\": \"Mohamed Tazi\", \"devise\": \"DH\"}"
```

**En XML :**
```bash
curl -X POST "http://localhost:8082/banque/comptes" \
  -H "Content-Type: application/xml" \
  -H "Accept: application/xml" \
  -d "<compte><solde>9200.0</solde><type>COURANT</type><titulaire>Fatima Zahra</titulaire><devise>DH</devise></compte>"
```

### 5. Mettre à jour un compte (PUT)
```bash
curl -X PUT "http://localhost:8082/banque/comptes/1" \
  -H "Content-Type: application/json" \
  -H "Accept: application/json" \
  -d "{\"solde\": 12000.0, \"type\": \"COURANT\", \"titulaire\": \"Amine El Amrani\", \"devise\": \"DH\"}"
```

### 6. Supprimer un compte (DELETE)
```bash
curl -X DELETE "http://localhost:8082/banque/comptes/3"
```

### 7. Endpoints Personnalisés
```bash
# Filtrer par type (COURANT ou EPARGNE)
curl -X GET "http://localhost:8082/banque/comptes/type/EPARGNE" -H "Accept: application/json"

# Effectuer un versement de 500 DH sur le compte 1
curl -X PUT "http://localhost:8082/banque/comptes/1/verser?montant=500" -H "Accept: application/json"

# Statistiques globales de la banque
curl -X GET "http://localhost:8082/banque/comptes/stats/solde-total" -H "Accept: application/json"
```

