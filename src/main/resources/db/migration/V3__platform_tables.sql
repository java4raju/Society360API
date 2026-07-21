-- ── Plans ────────────────────────────────────────────────────
CREATE TABLE plans (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name             VARCHAR(50)    NOT NULL,
    slug             VARCHAR(20)    NOT NULL UNIQUE,
    price_monthly    NUMERIC(10,2)  NOT NULL DEFAULT 0,
    price_yearly     NUMERIC(10,2)  NOT NULL DEFAULT 0,
    max_units        INT            NOT NULL,
    max_staff        INT            NOT NULL DEFAULT 5,
    razorpay_plan_id VARCHAR(100),
    is_active        BOOLEAN        NOT NULL DEFAULT TRUE
);

-- ── Tenants ──────────────────────────────────────────────────
CREATE TABLE tenants (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name            VARCHAR(200)   NOT NULL,
    slug            VARCHAR(50)    NOT NULL UNIQUE,
    rwa_reg_number  VARCHAR(100),
    address         TEXT,
    city            VARCHAR(100),
    pincode         CHAR(6),
    contact_name    VARCHAR(100)   NOT NULL,
    contact_email   VARCHAR(200)   NOT NULL,
    contact_phone   VARCHAR(15),
    status          VARCHAR(20)    NOT NULL DEFAULT 'TRIAL'
                        CHECK (status IN ('TRIAL','ACTIVE','SUSPENDED','CANCELLED')),
    plan_id         UUID           REFERENCES plans(id),
    trial_ends_at   TIMESTAMPTZ    NOT NULL,
    max_units       INT            NOT NULL DEFAULT 50,
    logo_url        TEXT,
    db_schema       VARCHAR(50)    NOT NULL DEFAULT 'public',
    email_verified  BOOLEAN        NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_tenants_slug   ON tenants(slug);
CREATE INDEX idx_tenants_status ON tenants(status);

-- ── Subscriptions ─────────────────────────────────────────────
CREATE TABLE subscriptions (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id            UUID NOT NULL UNIQUE REFERENCES tenants(id) ON DELETE CASCADE,
    plan_id              UUID NOT NULL REFERENCES plans(id),
    status               VARCHAR(20) NOT NULL DEFAULT 'TRIALING'
                             CHECK (status IN ('TRIALING','ACTIVE','PAST_DUE','CANCELLED')),
    billing_cycle        VARCHAR(10) NOT NULL DEFAULT 'MONTHLY'
                             CHECK (billing_cycle IN ('MONTHLY','YEARLY')),
    current_period_start TIMESTAMPTZ NOT NULL,
    current_period_end   TIMESTAMPTZ NOT NULL,
    razorpay_sub_id      VARCHAR(100) UNIQUE,
    razorpay_customer_id VARCHAR(100),
    cancelled_at         TIMESTAMPTZ,
    cancel_reason        TEXT,
    created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- ── Billing Events ────────────────────────────────────────────
CREATE TABLE billing_events (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id           UUID NOT NULL REFERENCES tenants(id),
    subscription_id     UUID REFERENCES subscriptions(id),
    event_type          VARCHAR(50)    NOT NULL,
    amount              NUMERIC(10,2)  NOT NULL DEFAULT 0,
    currency            CHAR(3)        NOT NULL DEFAULT 'INR',
    razorpay_payment_id VARCHAR(100),
    razorpay_order_id   VARCHAR(100),
    invoice_url         TEXT,
    raw_payload         JSONB,
    occurred_at         TIMESTAMPTZ    NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_billing_tenant ON billing_events(tenant_id, occurred_at DESC);

-- ── Platform Admins ───────────────────────────────────────────
CREATE TABLE platform_admins (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email         VARCHAR(200) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    name          VARCHAR(100) NOT NULL,
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

-- ── Tenant Invitations ────────────────────────────────────────
CREATE TABLE tenant_invitations (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    tenant_id   UUID NOT NULL REFERENCES tenants(id) ON DELETE CASCADE,
    email       VARCHAR(200) NOT NULL,
    role        VARCHAR(20)  NOT NULL DEFAULT 'RESIDENT',
    token_hash  VARCHAR(255) NOT NULL,
    expires_at  TIMESTAMPTZ  NOT NULL,
    accepted_at TIMESTAMPTZ,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);
