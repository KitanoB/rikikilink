CREATE TABLE links (
    id UUID PRIMARY KEY,
    code VARCHAR(8) NOT NULL UNIQUE,
    target_url VARCHAR(2048) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT now(),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    owner VARCHAR(64),
    type VARCHAR(20) NOT NULL DEFAULT 'PERMANENT'
);

CREATE INDEX idx_short_code ON links (code);