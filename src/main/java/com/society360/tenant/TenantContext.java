package com.society360.tenant;

import java.util.UUID;

/**
 * Carries the current tenant ID using ScopedValue — safe for virtual threads,
 * bounded to the request scope, and immutable within that scope.
 * ScopedValue became standard in Java 24 (JEP 487); no preview flag needed on Java 25.
 */
public final class TenantContext {

    public static final ScopedValue<UUID> TENANT_ID = ScopedValue.newInstance();

    private TenantContext() {}

    /** Returns the tenant ID bound in the current scope, or null if unbound. */
    public static UUID get() {
        return TENANT_ID.orElse(null);
    }

    /** Returns true when a tenant scope is active for the current execution. */
    public static boolean isPresent() {
        return TENANT_ID.isBound();
    }
}
