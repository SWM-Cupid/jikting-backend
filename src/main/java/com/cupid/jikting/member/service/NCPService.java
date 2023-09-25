package com.cupid.jikting.member.service;

import com.cupid.jikting.common.service.RedisConnector;
import com.cupid.jikting.common.util.VerificationCodeGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;

@RequiredArgsConstructor
public abstract class NCPService {

    protected static final int VERIFICATION_CODE_LENGTH = 6;
    protected static final int EXPIRE_TIME = 3;
    protected static final String CHARSET_NAME = "UTF-8";
    protected static final String SECRET_KEY_ALGORITHM = "HmacSHA256";
    protected static final String BLANK = " ";
    protected static final String NEW_LINE = "\n";

    protected final RedisConnector redisConnector;
    protected final ObjectMapper objectMapper;
    protected final RestTemplate restTemplate;

    @Value("${ncp.accessKey}")
    private String accessKey;

    @Value("${ncp.secretKey}")
    private String secretKey;

    protected String generateVerificationCode(String key) {
        String verificationCode = VerificationCodeGenerator.generate(VERIFICATION_CODE_LENGTH);
        redisConnector.set(key, verificationCode, Duration.ofMinutes(EXPIRE_TIME));
        return verificationCode;
    }

    protected String getVerificationCodeMessage(String verificationCode) {
        return "[직팅]\n인증번호: " + verificationCode;
    }

    protected HttpHeaders getHttpHeaders(String url) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        String time = String.valueOf(System.currentTimeMillis());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time);
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", getSignature(url, time));
        return headers;
    }

    private String getSignature(String url, String time) throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
        Mac mac = Mac.getInstance(SECRET_KEY_ALGORITHM);
        mac.init(new SecretKeySpec(secretKey.getBytes(CHARSET_NAME), SECRET_KEY_ALGORITHM));
        return Base64.encodeBase64String(mac.doFinal(getMessage(url, time).getBytes(CHARSET_NAME)));
    }

    private String getMessage(String url, String time) {
        return HttpMethod.POST.name() + BLANK + url
                + NEW_LINE + time
                + NEW_LINE + accessKey;
    }
}
