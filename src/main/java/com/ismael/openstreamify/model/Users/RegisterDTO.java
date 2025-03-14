package com.ismael.openstreamify.model.Users;

import com.ismael.openstreamify.enums.Provider;

public record RegisterDTO(
        String login,
        String password,
        UserRole role,
        String name,
        Provider provider
) {
}
