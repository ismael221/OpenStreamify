package com.ismael.movies.controller;

import com.ismael.movies.DTO.VerificationCodeDTO;
import com.ismael.movies.model.Users.User;
import com.ismael.movies.services.UserService;
import com.ismael.movies.services.VerificationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("api/v1/verify")
public class VerificationCodeController {

    @Autowired
    UserService userService;

    @Autowired
    VerificationCodeService verificationCodeService;

    @PostMapping
    public ResponseEntity<?> verifyRegisterCode(@RequestBody VerificationCodeDTO verificationCodeDTO)  {
        String email = verificationCodeDTO.getEmail();
        String code = verificationCodeDTO.getCode();
        boolean isVerified = verificationCodeService.verifyCode(email,code);

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
