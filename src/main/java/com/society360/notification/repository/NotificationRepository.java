package com.society360.notification.repository;

import com.society360.notification.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query("SELECT n FROM Notification n WHERE n.user.id = :userId OR n.user IS NULL ORDER BY n.createdAt DESC")
    Page<Notification> findForUser(@Param("userId") UUID userId, Pageable pageable);

    long countByUserIdAndReadFalse(UUID userId);

    @Query("SELECT COUNT(n) FROM Notification n WHERE (n.user IS NULL) AND n.read = false")
    long countBroadcastUnread();

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.id = :id AND (n.user.id = :userId OR n.user IS NULL)")
    int markRead(@Param("id") UUID id, @Param("userId") UUID userId);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.user.id = :userId OR n.user IS NULL")
    void markAllRead(@Param("userId") UUID userId);
}
