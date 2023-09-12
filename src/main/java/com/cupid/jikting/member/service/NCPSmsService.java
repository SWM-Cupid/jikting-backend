package com.cupid.jikting.member.service;

import com.cupid.jikting.common.service.RedisConnector;
import com.cupid.jikting.common.util.VerificationCodeGenerator;
import com.cupid.jikting.member.dto.SendSmsRequest;
import com.cupid.jikting.member.dto.SmsRequest;
import com.cupid.jikting.member.dto.SmsResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Service
public class NCPSmsService implements SmsService {

    private static final int VERIFICATION_CODE_LENGTH = 6;
    private static final int EXPIRE_TIME = 3;
    private static final String TYPE = "SMS";
    private static final String CHARSET_NAME = "UTF-8";
    private static final String SECRET_KEY_ALGORITHM = "HmacSHA256";
    private static final String BLANK = " ";
    private static final String NEW_LINE = "\n";

    private final RedisConnector redisConnector;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    @Value("${ncp.accessKey}")
    private String accessKey;

    @Value("${ncp.secretKey}")
    private String secretKey;

    @Value("${ncp.sms.serviceId}")
    private String serviceId;

    @Value("${ncp.sms.sender}")
    private String phone;

    @Override
    public SmsResponse sendSms(SendSmsRequest sendSmsRequest) throws JsonProcessingException, NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        return restTemplate.postForObject(
                URI.create("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages"),
                new HttpEntity<>(objectMapper.writeValueAsString(getSmsRequest(sendSmsRequest, generateVerificationCode(sendSmsRequest.getTo()))), getHttpHeaders()),
                SmsResponse.class);
    }

    private String generateVerificationCode(String phone) {
        String verificationCode = VerificationCodeGenerator.generate(VERIFICATION_CODE_LENGTH);
        redisConnector.set(phone, verificationCode, Duration.ofMinutes(EXPIRE_TIME));
        return verificationCode;
    }

    private SmsRequest getSmsRequest(SendSmsRequest sendSmsRequest, String verificationCode) {
        return SmsRequest.builder()
                .type(TYPE)
                .from(phone)
                .content(getVerificationCodeMessage(verificationCode))
                .messages(List.of(sendSmsRequest))
                .build();
    }

    private String getVerificationCodeMessage(String verificationCode) {
        return String.format("[직팅]\n인증번호: %s", verificationCode);
    }

    private HttpHeaders getHttpHeaders() throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String time = String.valueOf(System.currentTimeMillis());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", getSignature(time));
        return headers;
    }

    private String getSignature(String time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac mac = Mac.getInstance(SECRET_KEY_ALGORITHM);
        mac.init(new SecretKeySpec(secretKey.getBytes(CHARSET_NAME), SECRET_KEY_ALGORITHM));
        return Base64.encodeBase64String(mac.doFinal(getMessage(time).getBytes(CHARSET_NAME)));
    }

    private String getMessage(String time) {
        return HttpMethod.POST.name() + BLANK + String.format("/sms/v2/services/%s/messages", serviceId)
                + NEW_LINE + time
                + NEW_LINE + accessKey;
    }
}
