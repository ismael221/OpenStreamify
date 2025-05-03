package com.ismael.openstreamify.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VerificationCodeDTO implements Serializable {
    private String email;
    private String code;
}
