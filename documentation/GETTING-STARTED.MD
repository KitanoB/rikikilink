# 🚀 Getting Started | Bien démarrer avec RikikiLink

Welcome to **RikikiLink** – your self-hosted, real-time URL shortener.  
Bienvenue sur **RikikiLink** – votre raccourcisseur d’URL intelligent et auto-hébergé.

---

## 📋 Table of Contents | Table des matières

- [Requirements | Prérequis](#requirements--prérequis)
- [Start the Project | Démarrer le projet](#start-the-project--démarrer-le-projet)
- [Check Services Status | Vérifier que tout fonctionne)
- [Access Developer Tools | Accéder aux outils)
- [Stop the Project | Arrêter le projet)
- [Restart with Clean Build | Redémarrer proprement)
- [Tips | Astuces)
- [Troubleshooting | Dépannage)
- [Need Help? | Besoin d'aide ?)

---

## Requirements | Prérequis

- Docker 🐳 `v20+`
- Docker Compose `v2.0+`
- Make (Linux/macOS) or WSL (Windows)

---

## Start the Project | Démarrer le projet

```bash
make dev-up
```

This will:  
• Load the environment variables from `.env`  
• Launch PostgreSQL, Redis, and RikikiLink microservices  
• Create a shared Docker network and volume

Ceci va :  
• Charger les variables d’environnement depuis `.env`  
• Démarrer PostgreSQL, Redis et les microservices RikikiLink  
• Créer un réseau et un volume Docker partagés

---

## ✅ Check Services Status | Vérifier que tout fonctionne

```bash
make dev-logs
```

Expected messages:  
• PostgreSQL init process complete; ready for start up.  
• Redis ready to accept connections  
• Spring Boot apps: Started Application in X.XXX seconds

Messages attendus :  
• PostgreSQL prêt  
• Redis prêt  
• Microservices Spring démarrés (Started Application in ...)

You can also verify using Docker:  
Vous pouvez aussi vérifier avec Docker :

```bash
docker compose ps
```

---

## 🛠️ Access Developer Tools | Accéder aux outils

- PgAdmin: [http://localhost:5050](http://localhost:5050)  
- Credentials are defined in your `.env` file.  
- Les identifiants sont dans votre fichier `.env`.

---

## 🛑 Stop the Project | Arrêter le projet

```bash
make dev-down
```

This will:  
• Stop and remove all running containers  
• Keep your volumes (PostgreSQL data will persist)

Cela va :  
• Arrêter et supprimer les conteneurs en cours d’exécution  
• Conserver les volumes (les données PostgreSQL sont conservées)

---

## 🔄 Restart with Clean Build | Redémarrer proprement

```bash
make dev-restart
```

Will stop, rebuild, and restart everything from scratch.  
Cela arrête, reconstruit et relance tous les services depuis zéro.

---

## 💡 Tips | Astuces

- If you’re on Windows, use WSL2 for the best experience.  
- Si vous êtes sous Windows, utilisez WSL2 pour une meilleure expérience.
- Change `.env` values to customize ports or credentials.  
- Modifiez le fichier `.env` pour personnaliser les ports ou identifiants.
- Need to debug? Add `SPRING_PROFILES_ACTIVE=dev` to `.env`.  
- Besoin de déboguer ? Ajoutez `SPRING_PROFILES_ACTIVE=dev` dans `.env`.

---

## 🛠️ Troubleshooting | Dépannage

If something doesn’t work, check:  
Si quelque chose ne fonctionne pas, vérifiez :

- Docker logs (`make dev-logs`)
- That `.env` file is at project root  
- Que le fichier `.env` est à la racine du projet
- That `infra/docker-compose.yml` exists  
- Que `infra/docker-compose.yml` existe

If you’re still having trouble, see the [FAQ](#) or open an issue.  
Si le problème persiste, consultez la [FAQ](#) ou ouvrez une issue.

---

## 🙌 Need help? | Besoin d'aide ?

Feel free to open an issue or start a discussion on GitHub!  
N’hésitez pas à ouvrir une issue ou à démarrer une discussion sur GitHub !

---

Happy hacking 👨‍💻👩‍💻! | Bon codage 🚀 !
