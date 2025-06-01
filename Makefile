# ======================
# VARIABLES
# ======================

ENV_FILE=.env 
DOCKER_COMPOSE=docker compose -f infra/docker-compose/docker-compose.yml --env-file $(ENV_FILE)

# ======================
# COMMANDES DEV
# ======================

.PHONY: help dev-up dev-down dev-restart dev-logs db-migrate pgadmin

help:
	@echo "Available commands : "
	@echo "  make dev-up        → Start all services (docker-compose up -d)"
	@echo "  make dev-down      → Stop all services"
	@echo "  make dev-restart   → Restart all services"
	@echo "  make dev-logs      → Show logs"
	@echo "  make pgadmin       → Open PgAdmin in browser (http://localhost:5050)"

dev-up:
	$(DOCKER_COMPOSE) up -d

dev-down:
	$(DOCKER_COMPOSE) down

dev-restart:
	$(DOCKER_COMPOSE) down
	$(DOCKER_COMPOSE) up -d --build

dev-logs:
	$(DOCKER_COMPOSE) logs -f

pgadmin:
	open http://localhost:5050 || xdg-open http://localhost:5050 || echo "Open your browser and go to http://localhost:5050"
