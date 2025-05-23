# RikikiLink – Politique d'expiration des liens / Link Expiration Policy

Ce document explique le choix d'une stratégie d'expiration simple et explicite dans le cadre d'une architecture MVP limitée à \~60 heures de développement.

This document explains the chosen expiration strategy in the context of a \~60h MVP implementation, aiming for clarity, maintainability, and technical credibility.

---

## Objectif / Goal

* Éviter les redirections éternelles
* Ne pas ajouter de complexité de planification, de TTL, ou de tâches asynchrones
* Fournir une expiration automatique, visible et testable

---

## Stratégie retenue / Chosen Strategy

> Lorsqu'un lien est accédé, si sa date de création (`createdAt`) remonte à plus de 10 jours, il est désactivé automatiquement (`active = false`) dans la base de données, et la redirection est bloquée.

> When a link is accessed, if its creation date is older than 10 days, it is automatically marked as inactive (`active = false`) in the database, and the redirection is blocked.

### Implémentation côté service / In the LinkService

```java
if (link.getCreatedAt().isBefore(Instant.now().minus(10, ChronoUnit.DAYS))) {
    link.setActive(false);
    linkRepository.save(link);
    throw new LinkExpiredException(); // HTTP 410 Gone
}
```

* ✔️ Simple à tester
* ✔️ Pas de tâche planifiée
* ✔️ Facile à documenter côté API (statut = "expiré")

---

## Alternatives écartées / Alternatives Rejected

### TTL Redis

* Nécessite de tout basculer en cache mémoire
* Pas de persistance durable
* Brise le modèle PostgreSQL-centric

### Champ `expiresAt` + job planifié

* Implique un cron ou un scheduler (complexe à maintenir/tester)
* Nécessite un traitement batch
* Inutile pour une architecture mono-utilisateur légère

###  Nettoyage périodique (garbage collector)

* Job d’arrière-plan non nécessaire dans une démo MVP
* Complexité sans valeur ajoutée immédiate

---

## Documentation API

* Tous les endpoints GET `/l/{code}` retournent une **erreur HTTP 410 Gone** si le lien est désactivé manuellement ou automatiquement
* Le frontend `/links/:code` peut afficher un statut `expiré` ou `désactivé`

---

## Possibilités d’évolution / Future Extension

* Ajouter un champ `expiresAt` configurable (nullable)
* Permettre à l’utilisateur de définir une durée de validité personnalisée
* Implémenter un job de nettoyage en cas de montée en charge ou d’usage public

---

Ce mécanisme permet une expiration implicite robuste, sans sacrifier la simplicité de développement ni la lisibilité du code.

This mechanism enables robust implicit expiration without sacrificing developer simplicity or code clarity.

