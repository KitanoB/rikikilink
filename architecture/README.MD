# RikikiLink Architecture / Architecture de RikikiLink

This document explains the architecture of RikikiLink in both **English and French**, including each component and how they interact.

Ce document explique l'architecture de RikikiLink en **anglais et en français**, en détaillant chaque composant et leurs interactions.

---

## 🏙 Overview / Vue d'ensemble (C4 Level 2 - Containers)

RikikiLink is based on a microservice architecture with event-driven communication. It demonstrates backend skills (Spring Boot, Redis, PostgreSQL), frontend integration (Angular), and modern deployment practices (Docker, Kubernetes).

RikikiLink repose sur une architecture microservices avec communication évènementielle. Il met en valeur la maîtrise du backend (Spring Boot, Redis, PostgreSQL), de l'intégration frontend (Angular) et du déploiement moderne (Docker, Kubernetes).


```
                 +------------------+
                 |   Utilisateur    |  ← Créateur (SPA)
                 +--------+---------+
                          |
                          v
                 +-------------------+
                 |   Frontend SPA    |
                 |    (Angular)      |
                 +--------+----------+
                          |
                          v
+--------------------------+                +-------------------+
|   Spring Cloud Gateway   | <── GET /l/ ─── |     Visiteur      |
+------------+-------------+                +-------------------+
             |
     +-------+--------+
     |                |
     v                v
+------------+    +-------------+
| Link       |    | Stats       |
| Service    |    | Service     |
+------------+    +-------------+
     |                |
     v                v
+------------+    +-------------+
| PostgreSQL |    |   Redis     |
+------------+    +-------------+
```

---

## 🔍 Components / Composants

### 1. **Frontend SPA (Angular)**

EN: The user interface used by link creators to manage and view analytics. Communicates exclusively with the API Gateway using REST and SSE.
FR : L'interface utilisateur utilisée par les créateurs de liens pour gérer et consulter les statistiques. Elle communique exclusivement avec la passerelle API via REST et SSE.

**Why SSE? / Pourquoi SSE ?**
EN: Server-Sent Events (SSE) enable real-time delivery of click analytics to the SPA. It's simpler than WebSockets for unidirectional communication (server → client), and avoids inefficient polling.
FR : SSE permet d'afficher les statistiques en temps réel dans le frontend sans surcharger le backend. C'est plus simple que WebSocket pour une communication unidirectionnelle, et plus efficace que le polling régulier.

### 2. **Spring Cloud Gateway**

EN: The single entry point to the system. Routes all HTTP requests (from both the SPA and direct browser access) to backend services.
FR : Le point d'entrée unique du système. Redirige toutes les requêtes HTTP (provenant du SPA ou d'un navigateur) vers les services backend.

### 3. **Link Service**

EN: Handles creation and resolution of short links. Publishes click events to Redis. Uses PostgreSQL for persistence.
FR : Gère la création et la résolution des liens courts. Publie les événements de clic dans Redis. Utilise PostgreSQL pour la persistance.

### 4. **Stats Service**

EN: Listens to click events, aggregates analytics, and exposes them via SSE. Stores computed data in PostgreSQL.
FR : Écoute les événements de clics, agrège les statistiques et les expose via SSE. Stocke les données dans PostgreSQL.

### 5. **PostgreSQL**

EN: Main relational database for both services.
FR : Base de données relationnelle principale pour les deux services.

### 6. **Redis**

EN: Used as an event queue to decouple click logging from analytics processing.
FR : Utilisé comme file d'attente pour découpler la journalisation des clics du traitement analytique.

**Why Redis and not PostgreSQL directly? / Pourquoi Redis plutôt que PostgreSQL directement ?**
EN: Using Redis allows decoupling the redirection path from analytics. The link service remains fast and non-blocking (low latency), while the stats service processes clicks asynchronously. Redis absorbs spikes, ensures durability, and enables scalable processing.
FR : Redis permet de séparer la redirection (qui doit être ultra rapide) du traitement des statistiques (plus lourd). Le service de redirection reste léger et réactif, pendant que le stats service traite les clics en asynchrone. Redis sert de tampon en cas de pic de trafic, assure la résilience et facilite la scalabilité.

---

## 📈 Data Flow / Flux de données

### EN:

1. A user creates a link through the Angular SPA.
2. The SPA calls the API Gateway, which routes to the Link Service.
3. The Link Service generates and stores the short URL.
4. A visitor clicks the short URL (GET /l/{code}), which hits the Gateway.
5. The Gateway routes to the Link Service, which redirects the visitor and emits a click event.
6. The Stats Service consumes the event from Redis and updates analytics.
7. The SPA subscribes to SSE to display live stats.

### FR :

1. Un utilisateur crée un lien via le SPA Angular.
2. Le SPA appelle le Gateway, qui redirige vers le Link Service.
3. Le Link Service génère et stocke l'URL raccourcie.
4. Un visiteur clique sur le lien (GET /l/{code}), ce qui passe par le Gateway.
5. Le Gateway route la requête vers le Link Service, qui redirige le visiteur et publie un événement de clic.
6. Le Stats Service consomme l'événement depuis Redis et met à jour les statistiques.
7. Le SPA est abonné au flux SSE pour afficher les stats en direct.

---

## ✅ Resilience, Security and Observability / Résilience, sécurité et observabilité

### 🔒 Security / Sécurité

* Rate limiting on link creation (planned)
* Strict URL validation (RFC format, allowed domains)
* Optional blacklist of known malicious domains
* HTTP security headers via Gateway (HSTS, CSP)

### ⚙️ Resilience / Résilience

* Redis failure: redirection still works (event loss tolerated)
* 404 handling for unknown links
* Optional in-memory cache layer (Caffeine)
* Services are stateless and can be restarted independently

### 📊 Observability / Observabilité

* Metrics via Micrometer (latency, error rate, throughput)
* Structured JSON logging with trace IDs
* Health checks via Spring Boot Actuator (`/actuator/health`)
* Optional Prometheus + Grafana stack (roadmap)

---

## 🚀 Limitations and Scalability / Limites et Scalabilité

* Horizontal scaling supported via Docker/Kubernetes
* Redis can absorb traffic spikes for asynchronous analytics
* Gateway could be scaled or fronted by a K8s Ingress
* No retry/DLQ yet for Redis-to-PostgreSQL delivery (tradeoff assumed)

---

## 🌟 Project Scope / Objectif du projet

This project is not meant to support 10K RPS in production, but to demonstrate solid backend design, modern DevOps, and realistic event-driven architecture in a portfolio context.

Ce projet n'a pas vocation à supporter 10K req/s en production, mais à démontrer une architecture moderne, cohérente et industrialisable dans un contexte de portfolio.
