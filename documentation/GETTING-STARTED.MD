# 🚀 Getting Started | Bien démarrer avec RikikiLink

Welcome to **RikikiLink** – your self-hosted, real-time URL shortener.
Bienvenue sur **RikikiLink** – votre raccourcisseur d’URL intelligent et auto-hébergé.

---

## Requirements | Prérequis

- Docker 🐳 `v20+`
- Docker Compose `v2.0+`
- Make (Linux/macOS) or WSL (Windows)

---

## Start the project | Démarrer le projet

```bash
  make dev-up
```

This will:
	•	Load the environment variables from .env
	•	Launch PostgreSQL, Redis, and RikikiLink microservices
	•	Create a shared Docker network and volume

Ceci va :
	•	Charger les variables d’environnement depuis .env
	•	Démarrer PostgreSQL, Redis et les microservices RikikiLink
	•	Créer un réseau et un volume Docker partagés

⸻

✅ Check services status | Vérifier que tout fonctionne
```bash
  make dev-logs
```

Expected messages:
	•	PostgreSQL init process complete; ready for start up.
	•	Redis ready to accept connections
	•	Spring Boot apps: Started Application in X.XXX seconds

You can also verify using Docker:

```bash
  docker compose ps
```

Sortie attendue :
	•	PostgreSQL prêt
	•	Redis prêt
	•	Microservices Spring démarrés (Started Application in ...)

⸻

Access developer tools | Accéder aux outils
	•	PgAdmin: http://localhost:5050
Credentials are defined in your .env file.

⸻

Stop the project | Arrêter le projet

```bash
  make dev-down
```

This will:
	•	Stop and remove all running containers
	•	Keep your volumes (PostgreSQL data will persist)

Cela va :
	•	Arrêter et supprimer les conteneurs en cours d’exécution
	•	Conserver les volumes (les données PostgreSQL sont conservées)

⸻

Restart with clean build | Redémarrer proprement

```bash
  make dev-restart
```

Will stop, rebuild, and restart everything from scratch.

Cela arrête, reconstruit et relance tous les services depuis zéro.

⸻

Tips
	•	If you’re on Windows, use WSL2 for the best experience.
	•	Change .env values to customize ports or credentials.
	•	Need to debug? Add SPRING_PROFILES_ACTIVE=dev to .env.

⸻
 
Need help?

If something doesn’t work, check:
	•	Docker logs (make dev-logs)
	•	That .env file is at project root
	•	That infra/docker-compose.yml exists

⸻

Happy hacking 👨‍💻👩‍💻! | Bon codage 🚀 !