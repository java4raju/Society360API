package com.society360.tenant;

import com.society360.tenant.repository.TenantRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Resolves the tenant from the request's subdomain and binds it into a ScopedValue
 * for the duration of the request. The binding is automatically released when the
 * ScopedValue.Carrier scope exits — no manual cleanup needed.
 *
 * With virtual threads (Project Loom), each request runs on its own carrier thread
 * and ScopedValue propagates correctly into child threads / structured concurrency
 * tasks without any extra configuration.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class TenantFilter extends OncePerRequestFilter {

    private final TenantRepository tenantRepository;

    private static final Set<String> RESERVED_SLUGS = Set.of(
        "www", "api", "admin", "app", "mail", "static", "assets", "platform"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        String slug = extractSlug(request);
        UUID tenantId = resolveTenantId(slug);

        if (tenantId != null) {
            // Bind the tenant ID into a ScopedValue for the lifetime of this request.
            // ScopedValue.Carrier.call() accepts Callable<T> which propagates checked exceptions.
            try {
                ScopedValue.where(TenantContext.TENANT_ID, tenantId).call(() -> {
                    chain.doFilter(request, response);
                    return null;
                });
            } catch (ServletException | IOException e) {
                throw e;
            } catch (Exception e) {
                throw new ServletException("Unexpected error in tenant-scoped filter chain", e);
            }
        } else {
            // No tenant resolved (platform admin subdomain, localhost, health checks, etc.)
            // Proceed without binding a tenant scope.
            chain.doFilter(request, response);
        }
    }

    private UUID resolveTenantId(String slug) {
        if (slug == null || RESERVED_SLUGS.contains(slug)) {
            return null;
        }
        return tenantRepository.findBySlug(slug)
            .map(t -> t.getId())
            .orElse(null);
    }

    private String extractSlug(HttpServletRequest request) {
        String host = request.getServerName();
        if (host != null && host.contains(".")) {
            return host.split("\\.")[0];
        }
        return null;
    }
}
