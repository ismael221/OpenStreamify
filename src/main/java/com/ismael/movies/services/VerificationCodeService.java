package com.ismael.movies.services;

import com.ismael.movies.model.UserVerification;
import com.ismael.movies.repository.UserVerificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

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
