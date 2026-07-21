-- ============================================================
-- Society360 — Initial Schema (PostgreSQL)
-- ============================================================

-- ── Users ────────────────────────────────────────────────────
CREATE TABLE users (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email       VARCHAR(255) NOT NULL UNIQUE,
    password    VARCHAR(255) NOT NULL,
    name        VARCHAR(255) NOT NULL,
    role        VARCHAR(20)  NOT NULL CHECK (role IN ('ADMIN','RESIDENT','STAFF')),
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

-- ── Residents ────────────────────────────────────────────────
CREATE TABLE residents (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    block         VARCHAR(10)   NOT NULL,
    flat_number   VARCHAR(20)   NOT NULL,
    owner_name    VARCHAR(255)  NOT NULL,
    tenant_name   VARCHAR(255),
    contact       VARCHAR(20)   NOT NULL,
    email         VARCHAR(255)  NOT NULL,
    parking_slots INTEGER       NOT NULL DEFAULT 0,
    occupancy     VARCHAR(20)   NOT NULL DEFAULT 'OWNER' CHECK (occupancy IN ('OWNER','TENANT','VACANT')),
    status        VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE','INACTIVE')),
    dues_amount   NUMERIC(12,2) NOT NULL DEFAULT 0,
    joined_date   DATE,
    created_at    TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255),
    UNIQUE (block, flat_number)
);

-- ── Transactions ─────────────────────────────────────────────
CREATE TABLE transactions (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    date         DATE          NOT NULL,
    description  VARCHAR(500)  NOT NULL,
    category     VARCHAR(100)  NOT NULL,
    amount       NUMERIC(14,2) NOT NULL,
    type         VARCHAR(10)   NOT NULL CHECK (type IN ('INCOME','EXPENSE')),
    status       VARCHAR(20)   NOT NULL DEFAULT 'COMPLETED' CHECK (status IN ('COMPLETED','PENDING','FAILED')),
    method       VARCHAR(100)  NOT NULL DEFAULT 'Bank Transfer',
    bank_ref_id  VARCHAR(255)  UNIQUE,
    bank_synced  BOOLEAN       NOT NULL DEFAULT FALSE,
    created_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255)
);
CREATE INDEX idx_txn_date ON transactions(date DESC);
CREATE INDEX idx_txn_type ON transactions(type);

-- ── Complaints ───────────────────────────────────────────────
CREATE TABLE complaints (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    category      VARCHAR(100) NOT NULL,
    priority      VARCHAR(20)  NOT NULL DEFAULT 'Medium',
    status        VARCHAR(50)  NOT NULL DEFAULT 'Open',
    resident      VARCHAR(255) NOT NULL,
    flat_number   VARCHAR(20)  NOT NULL,
    assigned_to   VARCHAR(255),
    created_date  DATE,
    resolved_date DATE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255)
);

-- ── Projects ─────────────────────────────────────────────────
CREATE TABLE projects (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name        VARCHAR(255)  NOT NULL,
    description TEXT,
    category    VARCHAR(100)  NOT NULL,
    status      VARCHAR(50)   NOT NULL DEFAULT 'Proposed',
    budget      NUMERIC(14,2) NOT NULL DEFAULT 0,
    spent       NUMERIC(14,2) NOT NULL DEFAULT 0,
    progress    INTEGER       NOT NULL DEFAULT 0 CHECK (progress BETWEEN 0 AND 100),
    owner       VARCHAR(255),
    start_date  DATE,
    end_date    DATE,
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

-- ── Notices ──────────────────────────────────────────────────
CREATE TABLE notices (
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title     VARCHAR(255) NOT NULL,
    body      TEXT         NOT NULL,
    category  VARCHAR(100) NOT NULL,
    author    VARCHAR(255) NOT NULL,
    date      DATE         NOT NULL,
    pinned    BOOLEAN      NOT NULL DEFAULT FALSE,
    important BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

-- ── Vendors ──────────────────────────────────────────────────
CREATE TABLE vendors (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name             VARCHAR(255)  NOT NULL,
    category         VARCHAR(100)  NOT NULL,
    status           VARCHAR(20)   NOT NULL DEFAULT 'Active',
    contact          VARCHAR(20)   NOT NULL,
    email            VARCHAR(255),
    contract_value   NUMERIC(14,2) NOT NULL DEFAULT 0,
    rating           INTEGER       NOT NULL DEFAULT 3 CHECK (rating BETWEEN 1 AND 5),
    contract_start   DATE,
    contract_end     DATE,
    created_at       TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMPTZ   NOT NULL DEFAULT NOW(),
    created_by       VARCHAR(255),
    updated_by       VARCHAR(255)
);

-- ── Meetings ─────────────────────────────────────────────────
CREATE TABLE meetings (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title      VARCHAR(255) NOT NULL,
    type       VARCHAR(50)  NOT NULL DEFAULT 'Committee',
    status     VARCHAR(50)  NOT NULL DEFAULT 'Scheduled',
    date       DATE         NOT NULL,
    location   VARCHAR(255) NOT NULL,
    attendees  INTEGER      NOT NULL DEFAULT 0,
    agenda     JSONB        NOT NULL DEFAULT '[]',
    decisions  JSONB        NOT NULL DEFAULT '[]',
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);

-- ── Polls ────────────────────────────────────────────────────
CREATE TABLE polls (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    category    VARCHAR(100) NOT NULL,
    status      VARCHAR(20)  NOT NULL DEFAULT 'Active',
    end_date    DATE         NOT NULL,
    total_votes INTEGER      NOT NULL DEFAULT 0,
    options     JSONB        NOT NULL DEFAULT '[]',
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_by  VARCHAR(255),
    updated_by  VARCHAR(255)
);

CREATE TABLE poll_votes (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    poll_id    UUID NOT NULL REFERENCES polls(id) ON DELETE CASCADE,
    option_id  VARCHAR(36) NOT NULL,
    user_id    UUID NOT NULL REFERENCES users(id),
    voted_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (poll_id, user_id)   -- one vote per resident per poll
);

-- ── Maintenance ──────────────────────────────────────────────
CREATE TABLE maintenance_requests (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title         VARCHAR(255) NOT NULL,
    description   TEXT,
    category      VARCHAR(100) NOT NULL,
    priority      VARCHAR(20)  NOT NULL DEFAULT 'Medium',
    status        VARCHAR(50)  NOT NULL DEFAULT 'Open',
    resident      VARCHAR(255) NOT NULL,
    flat_number   VARCHAR(20)  NOT NULL,
    assigned_to   VARCHAR(255),
    scheduled_date DATE,
    resolved_date  DATE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255)
);

-- ── Documents ────────────────────────────────────────────────
CREATE TABLE documents (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name         VARCHAR(255) NOT NULL,
    file_name    VARCHAR(500) NOT NULL,
    file_path    VARCHAR(1000) NOT NULL,
    mime_type    VARCHAR(100) NOT NULL,
    size_bytes   BIGINT       NOT NULL,
    category     VARCHAR(100) NOT NULL,
    uploaded_by  VARCHAR(255) NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at   TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_by   VARCHAR(255),
    updated_by   VARCHAR(255)
);

-- ── Notifications ────────────────────────────────────────────
CREATE TABLE notifications (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id    UUID         REFERENCES users(id) ON DELETE CASCADE,
    title      VARCHAR(255) NOT NULL,
    body       TEXT         NOT NULL,
    type       VARCHAR(50)  NOT NULL DEFAULT 'info',
    read       BOOLEAN      NOT NULL DEFAULT FALSE,
    link       VARCHAR(500),
    created_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    created_by VARCHAR(255),
    updated_by VARCHAR(255)
);
CREATE INDEX idx_notif_user ON notifications(user_id, read);

-- ── Seed default admin user ──────────────────────────────────
-- Password: Admin@123 (BCrypt encoded — change in production!)
INSERT INTO users (email, password, name, role)
VALUES ('admin@society360.app',
        '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQJqhN8/LewdBPj5Xj1bKfJeO',
        'Society Admin', 'ADMIN');
