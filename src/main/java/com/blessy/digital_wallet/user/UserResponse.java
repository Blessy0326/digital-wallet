package com.blessy.digital_wallet.user;

import java.time.Instant;

public record UserResponse(Long id, String fullName, String email, Instant createdAt) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}