package com.society360.tenant.repository;

import com.society360.tenant.entity.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface PlanRepository extends JpaRepository<Plan, UUID> {
    Optional<Plan> findBySlug(String slug);
}
