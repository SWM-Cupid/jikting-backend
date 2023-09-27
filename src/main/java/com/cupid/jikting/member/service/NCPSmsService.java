package com.cupid.jikting.member.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.SmsSendFailException;
import com.cupid.jikting.common.service.RedisConnector;
import com.cupid.jikting.member.dto.SendSmsRequest;
import com.cupid.jikting.member.dto.SmsRequest;
import com.cupid.jikting.member.dto.SmsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class NCPSmsService extends NCPService implements SmsService {

    private static final String SMS_SEND_SUCCESS = "202";
    private static final String TYPE = "SMS";

    @Value("${ncp.sms.serviceId}")
    private String serviceId;

    @Value("${ncp.sms.sender}")
    private String phone;

    public NCPSmsService(RedisConnector redisConnector, ObjectMapper objectMapper, RestTemplate restTemplate) {
        super(redisConnector, objectMapper, restTemplate);
    }

    @Override
    public void sendSms(SendSmsRequest sendSmsRequest) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String url = "/sms/v2/services/" + serviceId + "/messages";
        SmsResponse smsResponse = restTemplate.postForObject(
                URI.create("https://sens.apigw.ntruss.com" + url),
                new HttpEntity<>(objectMapper.writeValueAsString(getSmsRequest(sendSmsRequest, generateVerificationCode(sendSmsRequest.getTo()))), getHttpHeaders(url)),
                SmsResponse.class);
        if (!smsResponse.getStatusCode().equals(SMS_SEND_SUCCESS)) {
            throw new SmsSendFailException(ApplicationError.SMS_SEND_FAIL);
        }
    }

    private SmsRequest getSmsRequest(SendSmsRequest sendSmsRequest, String verificationCode) {
        return SmsRequest.builder()
                .type(TYPE)
                .from(phone)
                .content(getVerificationCodeMessage(verificationCode))
                .messages(List.of(sendSmsRequest))
                .build();
    }
}
