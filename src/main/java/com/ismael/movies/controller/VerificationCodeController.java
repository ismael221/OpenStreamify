package com.ismael.movies.controller;

import com.ismael.movies.DTO.VerificationCodeDTO;
import com.ismael.movies.model.UserVerification;
import com.ismael.movies.model.Users.User;
import com.ismael.movies.model.VerificationCodeGenerator;
import com.ismael.movies.services.UserService;
import com.ismael.movies.services.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;


@RestController
@RequestMapping("api/v1/verify")
public class VerificationCodeController {

    final
    UserService userService;

    final
    VerificationCodeService verificationCodeService;

    public VerificationCodeController(UserService userService, VerificationCodeService verificationCodeService) {
        this.userService = userService;
        this.verificationCodeService = verificationCodeService;
    }

    @PostMapping
    public ResponseEntity<?> verifyRegisterCode(@RequestBody VerificationCodeDTO verificationCodeDTO)  {
        String email = verificationCodeDTO.getEmail();
        String code = verificationCodeDTO.getCode();
        UserVerification userVerification = verificationCodeService.checkCodeExpiration(email);
        boolean isVerified = verificationCodeService.verifyCode(email,code);

        //TODO VERIFY THE USER OUTPUT IF THE CODE HAS EXPIRED AND CHANGE IT ON THE DATABASE
        if (isVerified){
            User user = userService.findUserByLogin(email);
            user.setActive(true);
            User user1 =  userService.updateUserLogin(user);
            return ResponseEntity.ok(user1);
        }else {
            return ResponseEntity.badRequest().build();
        }
    }
}
