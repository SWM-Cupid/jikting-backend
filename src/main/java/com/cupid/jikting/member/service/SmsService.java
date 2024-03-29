package com.cupid.jikting.member.service;

import com.cupid.jikting.member.dto.SendSmsRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SmsService {

    void sendSms(SendSmsRequest sendSmsRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException;
}
