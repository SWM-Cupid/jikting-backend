package com.cupid.jikting.member.service;

import com.cupid.jikting.member.dto.SignUpVerificationCodeRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SmsService {

    void createVerificationCodeForSignup(SignUpVerificationCodeRequest signUpVerificationCodeRequest) throws JsonProcessingException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException;
}
