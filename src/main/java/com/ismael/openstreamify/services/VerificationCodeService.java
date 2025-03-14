package com.ismael.openstreamify.services;

import com.ismael.openstreamify.model.UserVerification;
import com.ismael.openstreamify.repository.UserVerificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {

    private final Logger logger = Logger.getLogger(VerificationCodeService.class.getName());

    private final UserVerificationRepository verificationRepository;

    public boolean verifyCode(String email, String code) {
        UserVerification userVerification = verificationRepository.findByEmail(email);

        //Checks if the generated code is in the 10 minute window
        Instant tenMinutesInFuture = Instant.now().plus(Duration.ofMinutes(10));

        boolean isValid = Instant.now().isAfter(userVerification.getGeneratedAt()) && Instant.now().isBefore(tenMinutesInFuture);

        if (userVerification != null && userVerification.getVerificationCode().equals(code) && isValid) {
            // Verification successful
            logger.info("Verification code is valid");
            return true;
        }
        // Verification failed
        logger.warning("Invalid verification code: " + code);
        return false;
    }

    public void saveVerificationCode(UserVerification code) {
        verificationRepository.save(code);
    }

    public UserVerification checkCodeExpiration(String email) {
        return verificationRepository.findByEmail(email);
    }

}
