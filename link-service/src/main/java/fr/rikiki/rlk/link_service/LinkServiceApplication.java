package fr.rikiki.rlk.link_service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class LinkServiceApplication {

    private final static Logger LOGGER = LoggerFactory.getLogger(LinkServiceApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LinkServiceApplication.class, args);
        LOGGER.debug("LinkService Starts...");
    }

}
