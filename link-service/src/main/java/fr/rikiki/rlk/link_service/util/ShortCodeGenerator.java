package fr.rikiki.rlk.link_service.util;

/**
 * Interface for generating short codes.
 * Implementations should provide a method to generate a unique short code.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
public interface ShortCodeGenerator {
    String generate();
}
