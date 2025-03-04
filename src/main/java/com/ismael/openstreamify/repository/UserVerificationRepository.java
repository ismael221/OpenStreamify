package com.ismael.openstreamify.repository;

import com.ismael.openstreamify.model.UserVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserVerificationRepository extends JpaRepository<UserVerification, UUID> {
    UserVerification findByEmail(String email);
}
