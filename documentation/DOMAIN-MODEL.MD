# RikikiLink - Domain Model (MVP)

This document describes the domain model used in the MVP version of RikikiLink. It is intentionally simple, scalable, and focused on demonstrating core backend skills in a constrained time (\~60h).

---

## Goals

* Provide a clear, minimal model for link creation and click tracking
* Enable real-time analytics (SSE) without full persistence of every click
* Avoid premature optimization or overengineering

---

## Entity: `Link`

Represents a shortened URL.

```java
@Entity
@Table(name = "links", indexes = {
    @Index(name = "idx_short_code", columnList = "code", unique = true)
})
public class Link {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 8)
    private String code;

    @Column(nullable = false, length = 2048)
    private String targetUrl;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    @Column(nullable = false)
    private boolean active = true;
}
```

### Rationale

* Simple: no click count, no expiration logic
* Easily indexed by `code`
* Can be extended later (title, tags, user, expiration, etc.)

---

## DTO: `ClickEvent`

Represents a click on a short link, emitted from the `link-service` to `stats-service` via Redis.

```java
public record ClickEvent(
    String linkCode,
    Instant timestamp,
    String referer,
    String ipHash
) {}
```

### Rationale

* Contains the essentials for stats aggregation
* Allows async, non-blocking redirection
* No need to persist each event in DB (processed in memory)

---

## Real-Time Stats Structure (in-memory only)

```json
{
  "totalClicks": 431,
  "clicksLastMinute": 12,
  "topReferers": [
    "https://google.com",
    "https://linkedin.com"
  ]
}
```

* Exposed via `/sse/links/:code`
* Cached in memory for 15–60 seconds
* Updated by `stats-service` in response to `ClickEvent`

---

## What’s deliberately omitted (MVP scope)

| Not included      | Why                                  |
| ----------------- | ------------------------------------ |
| `DailyStats`      | Requires aggregation jobs + storage  |
| Device/browser    | Adds parsing logic / payload size    |
| GeoIP             | Extra dependency, not core to demo   |
| Click persistence | Not necessary for real-time insights |

---

## Future evolution (possible extensions)

* Add `ClickEvent` persistence in PostgreSQL or S3 for audits
* Introduce daily/hourly pre-aggregates (`DailyStats`)
* Track GeoIP and device/browser using user-agent parser
* Extend `Link` with owner (API key) or expiration logic

---

