package fr.rikiki.rlk.link_service.repository;

import fr.rikiki.rlk.link_service.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface LinkRepository extends JpaRepository<Link, UUID> {
    Optional<Link> findByCode(String code);
}