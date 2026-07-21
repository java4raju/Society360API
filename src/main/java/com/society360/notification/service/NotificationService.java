package com.society360.notification.service;

import com.society360.auth.entity.User;
import com.society360.auth.repository.UserRepository;
import com.society360.common.dto.PageResponse;
import com.society360.common.exception.ResourceNotFoundException;
import com.society360.notification.dto.BroadcastRequest;
import com.society360.notification.dto.NotificationResponse;
import com.society360.notification.entity.Notification;
import com.society360.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public PageResponse<NotificationResponse> listForCurrentUser(int page, int size) {
        User user = currentUser();
        var pageable = PageRequest.of(page, size);
        return PageResponse.of(notificationRepository.findForUser(user.getId(), pageable)
            .map(NotificationResponse::from));
    }

    public Map<String, Long> unreadCount() {
        User user = currentUser();
        long count = notificationRepository.countByUserIdAndReadFalse(user.getId())
            + notificationRepository.countBroadcastUnread();
        return Map.of("unread", count);
    }

    @Transactional
    public void markRead(UUID id) {
        User user = currentUser();
        notificationRepository.markRead(id, user.getId());
    }

    @Transactional
    public void markAllRead() {
        notificationRepository.markAllRead(currentUser().getId());
    }

    @Transactional
    public void broadcast(BroadcastRequest req) {
        Notification notification = Notification.builder()
            .title(req.title())
            .body(req.body())
            .type(req.type() != null ? req.type() : "info")
            .link(req.link())
            .user(null)  // null = broadcast to everyone
            .build();
        notificationRepository.save(notification);
    }

    @Transactional
    public void sendToUser(UUID userId, String title, String body, String type, String link) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        Notification notification = Notification.builder()
            .title(title).body(body).type(type).link(link).user(user)
            .build();
        notificationRepository.save(notification);
    }

    private User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("User", email));
    }
}
