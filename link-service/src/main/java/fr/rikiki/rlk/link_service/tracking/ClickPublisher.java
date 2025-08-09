package fr.rikiki.rlk.link_service.tracking;

import org.slf4j.Logger;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for publishing click events to a Redis channel.
 * This service is responsible for sending click events to a specified Redis channel
 * for further processing or analytics.
 *
 * @author Kitano
 * @version 1.0
 * @since 1.0
 */
@Service
public class ClickPublisher {

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ClickPublisher.class);

    /**
     * Redis template for publishing messages.
     * This template is used to interact with Redis and publish messages to a channel.
     */
    private final StringRedisTemplate redisTemplate;

    /**
     * The Redis channel to which click events are published.
     * This channel is used to broadcast click events for further processing.
     */
    private static final String CHANNEL = "link.clicks";

    public ClickPublisher(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Publish a single click event (just the code for now).
     */
    public void publishClick(String code) {
        LOGGER.debug("Publishing click event for code: {}", code);
        redisTemplate.convertAndSend(CHANNEL, code);
    }
}
