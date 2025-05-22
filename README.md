<img src="./images/rikikilogo.png" width=30% height=30%>

### 🇫🇷 À propos de RikikiLink

RikikiLink est un raccourcisseur d’URL auto-hébergé, piloté par les événements et doté d’analyses en temps réel. Créez des liens courts personnalisés, partagez-les, et suivez leur performance depuis une interface moderne. Construit sur une architecture microservices, déployable via Docker/Kubernetes, RikikiLink reflète une conception backend robuste et une réelle aptitude à la production.

### 🇬🇧 About RikikiLink

RikikiLink is a self-hosted, event-driven URL shortener with real-time analytics. Easily create custom short links, share them, and monitor their performance through a modern web interface. Built with a microservice architecture and fully deployable via Docker/Kubernetes, RikikiLink demonstrates clean backend design and production readiness.

---

### 🇫🇷 Fonctionnalités Clés

#### ✅ Création de liens courts

* Génère des codes courts uniques (Base62)
* Interface API REST et formulaire web
* Stockage sécurisé dans PostgreSQL
* Détection optionnelle des doublons

#### ✅ Service de redirection rapide

* Redirection via `GET /l/{code}` (HTTP 301/302)
* Journalisation des clics de manière asynchrone

#### ✅ Suivi des clics & analyses

* Émission d'événements vers Redis
* Traitement par `stats-service` : timestamp, référent, IP hashée, code
* Agrégation des données dans PostgreSQL avec cache

#### ✅ Vue d’analyse en temps réel

* Données diffusées en SSE
* Graphiques dynamiques, top référents, clics totaux

#### ✅ Interface utilisateur Angular

* Création, gestion et copie de liens
* Vue stat live pour chaque lien
* UI responsive, packagée Docker

---

### 🇬🇧 Core Functionality

#### ✅ Short link creation

* Generates unique Base62 codes
* REST API and web form interface
* Secure PostgreSQL storage
* Optional duplicate detection

#### ✅ Fast redirection service

* Redirect via `GET /l/{code}` (HTTP 301/302)
* Asynchronous click logging

#### ✅ Click tracking & analytics

* Redis events per click
* Stats processing: timestamp, referrer, hashed IP, link code
* PostgreSQL storage with caching

#### ✅ Real-time analytics view

* Data via SSE
* Live charts, top referrers, total click count

#### ✅ Angular frontend

* Create, manage, and copy links
* Live stats per link
* Responsive Dockerized UI

---

### 📁 Infrastructure & DevOps

|                    | FR                                      | EN                                            |
| ------------------ | --------------------------------------- | --------------------------------------------- |
| Backend            | Spring Boot (Java 21), archi hexagonale | Spring Boot (Java 21), hexagonal architecture |
| Frontend           | Angular 17                              | Angular 17                                    |
| Database           | PostgreSQL                              | PostgreSQL                                    |
| Queue              | Redis (Pub/Sub)                         | Redis (Pub/Sub)                               |
| Real-time delivery | SSE                                     | SSE                                           |
| API Gateway        | Spring Cloud Gateway                    | Spring Cloud Gateway                          |
| Containerization   | Docker Compose                          | Docker Compose                                |
| K8s readiness      | Helm chart + manifests inclus           | Includes Helm chart + manifests               |
| CI/CD              | GitHub Actions (build, test, deploy)    | GitHub Actions (build, test, deploy)          |
| Observability      | Prometheus + Grafana intégrés           | Integrated Prometheus + Grafana               |

---

### 📆 Use cases / Cas d’utilisation

* Raccourcisseur personnel ou interne pour blog, portfolio, équipe
* Outil de démo pour architecture événementielle et suivi temps réel
* Dev/test tool to showcase click tracking + EDD

---

### 📖 Getting Started / Comment démarrer

*(à compléter : instructions pour cloner, configurer, et lancer localement avec Docker Compose)*

---

### 🏠 Deployment / Déploiement sur Homelab

*(à compléter : prérequis, déploiement Docker ou Kubernetes)*

---

### ⚙️ Technologies

*(à compléter : Java, Spring Boot, Angular, PostgreSQL, Redis, Docker, Kubernetes...)*

---

### 📀 Demonstration / Démonstration

*(à compléter : capture, GIF ou vidéo 20 s)*

---

### 👤 Author / Auteur

**Your Name** — [GitHub Profile](https://github.com/KitanoB)

---

### ✉️ License

This project is licensed under the [MIT License](https://opensource.org/licenses/MIT).
