package com.cupid.jikting.member.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface MailService {

    void sendMail(String name, String email) throws Exception;
}
