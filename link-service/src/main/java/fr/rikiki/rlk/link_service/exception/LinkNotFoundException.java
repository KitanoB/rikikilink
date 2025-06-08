package fr.rikiki.rlk.link_service.exception;

import org.slf4j.Logger;

/**
 * Exception thrown when a link with the specified code is not found.
 * This exception is used to indicate that a requested link does not exist in the system.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
public class LinkNotFoundException extends RuntimeException {
    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(LinkNotFoundException.class);
    public LinkNotFoundException(String code) {
        super("Link not found: " + code);
        LOGGER.error("Link not found: {}", code);
    }
}