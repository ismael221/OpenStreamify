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
    @Autowired
    private UserVerificationRepository verificationRepository;

    public boolean verifyCode(String email, String code) {
        UserVerification userVerification = verificationRepository.findByEmail(email);

        //Verifica se o codigo gerado esta na janela de 10 minutos atrás
        Instant tenMinutesInFuture = Instant.now().plus(Duration.ofMinutes(10));
        Instant instantToCompare  = userVerification.getGeneratedAt();

        Date geradoEm = Date.from(instantToCompare);
        Date expiraEm = Date.from(tenMinutesInFuture);

        SimpleDateFormat formatter = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
        String formattedVencimentoEm = formatter.format(expiraEm);
        String formattedGeradoEm= formatter.format(geradoEm);

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
