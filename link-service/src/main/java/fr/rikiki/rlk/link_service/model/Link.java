package fr.rikiki.rlk.link_service.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.UUID;

@Data
@Entity
@Table(name = "links", indexes = {
        @Index(name = "idx_short_code", columnList = "code", unique = true)
})
@EntityListeners(AuditingEntityListener.class)
public class Link {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 2048)
    private String targetUrl;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private boolean active = true;

    @Column(length = 64)
    private String owner;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LinkType type = LinkType.PERMANENT;

    public enum LinkType {
        PERMANENT, TEMPORARY
    }

}