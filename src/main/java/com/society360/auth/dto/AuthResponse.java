package com.society360.auth.dto;

import com.society360.auth.entity.User;

public record AuthResponse(
    String accessToken,
    String refreshToken,
    UserInfo user
) {
    public record UserInfo(String id, String name, String email, String role) {}

    public static AuthResponse of(String accessToken, String refreshToken, User user) {
        return new AuthResponse(
            accessToken,
            refreshToken,
            new UserInfo(user.getId().toString(), user.getName(), user.getEmail(), user.getRole().name())
        );
    }
}
