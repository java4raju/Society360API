package com.society360.tenant.service;

import com.society360.auth.entity.User;
import com.society360.auth.repository.UserRepository;
import com.society360.common.exception.BusinessException;
import com.society360.tenant.dto.RegistrationRequest;
import com.society360.tenant.dto.RegistrationResponse;
import com.society360.tenant.entity.Plan;
import com.society360.tenant.entity.Subscription;
import com.society360.tenant.entity.Tenant;
import com.society360.tenant.repository.PlanRepository;
import com.society360.tenant.repository.SubscriptionRepository;
import com.society360.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TenantRegistrationService {

    private final TenantRepository tenantRepository;
    private final PlanRepository planRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.base-domain:society360.in}")
    private String baseDomain;

    private static final Set<String> RESERVED_SLUGS = Set.of(
        "www", "api", "admin", "app", "mail", "static", "assets", "platform"
    );

    @Transactional
    public RegistrationResponse register(RegistrationRequest req) {
        if (RESERVED_SLUGS.contains(req.slug())) {
            throw new BusinessException("Slug '" + req.slug() + "' is reserved.");
        }
        if (tenantRepository.existsBySlug(req.slug())) {
            throw new BusinessException("Subdomain '" + req.slug() + "' is already taken.");
        }

        Plan trialPlan = planRepository.findBySlug("trial")
            .orElseThrow(() -> new BusinessException("Trial plan not configured. Contact support."));

        Instant now = Instant.now();
        Tenant tenant = Tenant.builder()
            .name(req.societyName())
            .slug(req.slug())
            .rwaRegNumber(req.rwaRegNumber())
            .address(req.address())
            .city(req.city())
            .pincode(req.pincode())
            .contactName(req.adminName())
            .contactEmail(req.adminEmail())
            .contactPhone(req.adminPhone())
            .maxUnits(req.unitCount())
            .trialEndsAt(now.plus(30, ChronoUnit.DAYS))
            .build();
        tenantRepository.save(tenant);

        Subscription sub = Subscription.builder()
            .tenant(tenant)
            .plan(trialPlan)
            .status(Subscription.SubStatus.TRIALING)
            .currentPeriodStart(now)
            .currentPeriodEnd(now.plus(30, ChronoUnit.DAYS))
            .build();
        subscriptionRepository.save(sub);

        User admin = User.builder()
            .email(req.adminEmail())
            .password(passwordEncoder.encode(req.adminPassword()))
            .name(req.adminName())
            .role(User.Role.ADMIN)
            .tenantId(tenant.getId())
            .build();
        userRepository.save(admin);

        return new RegistrationResponse(
            tenant.getId(),
            tenant.getSlug(),
            "https://" + tenant.getSlug() + "." + baseDomain,
            "Registration successful! Please check your email to verify your account. Your 30-day trial starts now."
        );
    }
}
