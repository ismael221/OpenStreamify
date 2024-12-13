package com.ismael.movies.model.Users;

import com.ismael.movies.enums.Provider;

public record RegisterDTO(
        String login,
        String password,
        UserRole role,
        String name,
        Provider provider
) {
}
