package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRequest;
import com.cupid.jikting.common.service.RedisConnector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Primary
@Transactional
@Service
public class RedisChattingService implements ChattingService {

    private static final String CHATTING_ROOM = "CHATTING_ROOM";
    private static final String DELIMITER = ":";

    private final RedisPublisher redisPublisher;
    private final RedisMessageListener redisMessageListener;
    private final RedisConnector redisConnector;

    @Override
    public void sendMessage(Long chattingRoomId, ChattingRequest chattingRequest) {
        redisPublisher.publish(redisMessageListener.getTopic(chattingRoomId), chattingRequest);
        redisConnector.saveChatting(CHATTING_ROOM + DELIMITER + chattingRoomId, chattingRequest.toChatting());
    }
}
