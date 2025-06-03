#!/bin/bash

# Nom du service à logger (Docker container name)
SERVICE_NAME=${1:-rikikilink-link-service}

# Nom du fichier à suivre (par défaut le rikikilink.log dans le volume monté)
LOG_FILE_PATH="/app/logs/rikikilink.log"

# Vérifie si le conteneur est en cours d'exécution
if ! docker ps --format '{{.Names}}' | grep -q "$SERVICE_NAME"; then
  echo "❌ Le conteneur $SERVICE_NAME ne tourne pas."
  exit 1
fi

# Test si le fichier existe à l'intérieur du conteneur
if ! docker exec "$SERVICE_NAME" test -f "$LOG_FILE_PATH"; then
  echo "⚠️  Le fichier $LOG_FILE_PATH n'existe pas dans le conteneur $SERVICE_NAME"
  echo "Essayez d’utiliser les logs stdout via : docker logs -f $SERVICE_NAME"
  exit 1
fi

# Stream les logs du fichier depuis le conteneur
echo "📄 Affichage des logs depuis $LOG_FILE_PATH dans $SERVICE_NAME"
docker exec -it "$SERVICE_NAME" tail -f "$LOG_FILE_PATH"
