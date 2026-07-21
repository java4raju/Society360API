-- Row-Level Security on all tenant tables
-- The app sets: SET LOCAL app.current_tenant_id = '<uuid>'

ALTER TABLE users               ENABLE ROW LEVEL SECURITY;
ALTER TABLE residents           ENABLE ROW LEVEL SECURITY;
ALTER TABLE transactions        ENABLE ROW LEVEL SECURITY;
ALTER TABLE complaints          ENABLE ROW LEVEL SECURITY;
ALTER TABLE projects            ENABLE ROW LEVEL SECURITY;
ALTER TABLE notices             ENABLE ROW LEVEL SECURITY;
ALTER TABLE vendors             ENABLE ROW LEVEL SECURITY;
ALTER TABLE meetings            ENABLE ROW LEVEL SECURITY;
ALTER TABLE polls               ENABLE ROW LEVEL SECURITY;
ALTER TABLE poll_votes          ENABLE ROW LEVEL SECURITY;
ALTER TABLE maintenance_requests ENABLE ROW LEVEL SECURITY;
ALTER TABLE documents           ENABLE ROW LEVEL SECURITY;
ALTER TABLE notifications       ENABLE ROW LEVEL SECURITY;

-- Allow superuser and platform_admin to bypass
-- The application role (society360_app) is subject to policies

CREATE POLICY tenant_isolation ON users
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON residents
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON transactions
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON complaints
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON projects
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON notices
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON vendors
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON meetings
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON polls
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON poll_votes
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON maintenance_requests
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON documents
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
CREATE POLICY tenant_isolation ON notifications
    USING (tenant_id::text = current_setting('app.current_tenant_id', true));
