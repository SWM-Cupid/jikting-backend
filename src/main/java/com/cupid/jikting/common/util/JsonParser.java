package com.cupid.jikting.common.util;

import com.cupid.jikting.chatting.dto.ChattingRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JsonParser {

    private final ObjectMapper objectMapper;

    public String toJson(ChattingRequest chattingRequest) {
        try {
            return objectMapper.writeValueAsString(chattingRequest);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ChattingRequest toChattingRequest(String chattingMessage) {
        try {
            return objectMapper.readValue(chattingMessage, ChattingRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
