-- Add a default tenant for existing data (dev/staging)
-- In production this runs on a fresh DB so existing data concern doesn't apply

-- Add tenant_id to all domain tables
ALTER TABLE users              ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE residents          ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE transactions       ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE complaints         ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE projects           ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE notices            ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE vendors            ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE meetings           ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE polls               ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE poll_votes          ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE maintenance_requests ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE documents          ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;
ALTER TABLE notifications      ADD COLUMN tenant_id UUID REFERENCES tenants(id) ON DELETE CASCADE;

-- Indexes on tenant_id for all tables
CREATE INDEX idx_users_tenant         ON users(tenant_id);
CREATE INDEX idx_residents_tenant     ON residents(tenant_id);
CREATE INDEX idx_transactions_tenant  ON transactions(tenant_id);
CREATE INDEX idx_complaints_tenant    ON complaints(tenant_id);
CREATE INDEX idx_projects_tenant      ON projects(tenant_id);
CREATE INDEX idx_notices_tenant       ON notices(tenant_id);
CREATE INDEX idx_vendors_tenant       ON vendors(tenant_id);
CREATE INDEX idx_meetings_tenant      ON meetings(tenant_id);
CREATE INDEX idx_polls_tenant         ON polls(tenant_id);
CREATE INDEX idx_poll_votes_tenant    ON poll_votes(tenant_id);
CREATE INDEX idx_maint_tenant         ON maintenance_requests(tenant_id);
CREATE INDEX idx_documents_tenant     ON documents(tenant_id);
CREATE INDEX idx_notifications_tenant ON notifications(tenant_id);

-- Fix unique constraints to be per-tenant
ALTER TABLE residents DROP CONSTRAINT IF EXISTS residents_block_flat_number_key;
ALTER TABLE residents ADD CONSTRAINT uq_residents_tenant_block_flat UNIQUE (tenant_id, block, flat_number);
