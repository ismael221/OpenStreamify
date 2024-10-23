package com.ismael.movies.repository;

import com.ismael.movies.model.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserVerificationRepository extends JpaRepository<UserVerification, UUID> {
    UserVerification findByEmail(String email);
}
