package com.cupid.jikting.common.util;

import com.cupid.jikting.chatting.dto.ChattingRequest;
import com.cupid.jikting.chatting.entity.Chatting;
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

    public ChattingRequest toChattingRequest(String chatting) {
        try {
            return objectMapper.readValue(chatting, ChattingRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public Chatting toChatting(String chatting) {
        try {
            return objectMapper.readValue(chatting, Chatting.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
