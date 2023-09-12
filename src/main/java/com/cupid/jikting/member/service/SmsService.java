package com.cupid.jikting.member.service;

import com.cupid.jikting.member.dto.SendSmsRequest;
import com.cupid.jikting.member.dto.SmsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface SmsService {

    SmsResponse sendSms(SendSmsRequest sendSmsRequest) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException;
}
