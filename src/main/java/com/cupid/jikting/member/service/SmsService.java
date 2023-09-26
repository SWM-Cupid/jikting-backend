package com.cupid.jikting.member.service;

import com.cupid.jikting.member.dto.SendSmsRequest;

public interface SmsService {

    void sendSms(SendSmsRequest sendSmsRequest) throws Exception;
}
