package com.cupid.jikting.member.service;

import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.MailSendFailException;
import com.cupid.jikting.common.service.RedisConnector;
import com.cupid.jikting.member.dto.MailRequest;
import com.cupid.jikting.member.dto.MailRequestRecipient;
import com.cupid.jikting.member.dto.MailResponse;
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
public class NCPMailService extends NCPService implements MailService {

    private static final String TYPE = "R";
    private static final String MAIL_TITLE = "[" + SERVICE_NAME + "] 회사 인증번호 안내 메일";

    @Value("${ncp.mail.senderAddress}")
    private String senderAddress;

    public NCPMailService(RedisConnector redisConnector, ObjectMapper objectMapper, RestTemplate restTemplate) {
        super(redisConnector, objectMapper, restTemplate);
    }

    @Override
    public void sendMail(String name, String email) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException, JsonProcessingException {
        MailResponse mailResponse = restTemplate.postForObject(
                URI.create("https://mail.apigw.ntruss.com/api/v1/mails"),
                new HttpEntity<>(objectMapper.writeValueAsString(getMailRequest(name, email, generateVerificationCode(email))), getHttpHeaders("/api/v1/mails")),
                MailResponse.class);
        if (mailResponse.getCount() != 1) {
            throw new MailSendFailException(ApplicationError.MAIL_SEND_FAIL);
        }
    }

    private MailRequest getMailRequest(String name, String email, String verificationCode) {
        return MailRequest.builder()
                .title(MAIL_TITLE)
                .body(getVerificationCodeMessage(verificationCode))
                .senderAddress(senderAddress)
                .senderName(SERVICE_NAME)
                .recipients(List.of(MailRequestRecipient.of(name, email, TYPE)))
                .build();
    }
}
