package com.ismael.openstreamify.services;

import com.ismael.openstreamify.model.UserVerification;
import com.ismael.openstreamify.repository.UserVerificationRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class VerificationCodeService {

    private final UserVerificationRepository verificationRepository;

    public VerificationCodeService(UserVerificationRepository verificationRepository) {
        this.verificationRepository = verificationRepository;
    }

    public boolean verifyCode(String email, String code) {
        UserVerification userVerification = verificationRepository.findByEmail(email);

        //Checks if the generated code is in the 10 minute window
        Instant tenMinutesInFuture = Instant.now().plus(Duration.ofMinutes(10));

        boolean isValid = Instant.now().isAfter(userVerification.getGeneratedAt()) && Instant.now().isBefore(tenMinutesInFuture);


        if (userVerification != null && userVerification.getVerificationCode().equals(code) && isValid) {
            // Verification successful
            return true;
        }
        // Verification failed
        return false;
    }

    public void saveVerificationCode(UserVerification code){
        verificationRepository.save(code);
    }

    public UserVerification checkCodeExpiration(String email){
        return verificationRepository.findByEmail(email);
    }

}
