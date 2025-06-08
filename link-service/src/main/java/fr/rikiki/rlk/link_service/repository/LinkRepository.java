package fr.rikiki.rlk.link_service.repository;

import fr.rikiki.rlk.link_service.model.Link;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for managing Link entities.
 * Provides methods to perform CRUD operations and custom queries.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
public interface LinkRepository extends JpaRepository<Link, UUID> {
    Optional<Link> findByCode(String code);
}