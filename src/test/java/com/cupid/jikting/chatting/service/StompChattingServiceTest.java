package com.cupid.jikting.chatting.service;

import com.cupid.jikting.chatting.dto.ChattingRequest;
import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.chatting.repository.ChattingRoomRepository;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import com.cupid.jikting.member.entity.MemberProfile;
import com.cupid.jikting.member.repository.MemberProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.willReturn;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class StompChattingServiceTest {

    private static final Long ID = 1L;
    private static final String CONTENT = "채팅 내용";

    private MemberProfile memberProfile;
    private ChattingRoom chattingRoom;
    private ChattingRequest chattingRequest;

    @InjectMocks
    private StompChattingService chattingService;

    @Mock
    private MemberProfileRepository memberProfileRepository;

    @Mock
    private ChattingRoomRepository chattingRoomRepository;

    @BeforeEach
    void setUp() {
        chattingRequest = ChattingRequest.builder()
                .senderId(ID)
                .content(CONTENT)
                .build();
        memberProfile = MemberProfile.builder()
                .id(ID)
                .build();
        chattingRoom = ChattingRoom.builder()
                .id(ID)
                .build();
    }

    @Test
    void 채팅_메시지_전송_성공() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willReturn(Optional.of(chattingRoom)).given(chattingRoomRepository).findById(anyLong());
        willReturn(chattingRoom).given(chattingRoomRepository).save(any(ChattingRoom.class));
        // when
        chattingService.sendMessage(ID, chattingRequest);
        // then
        assertAll(
                () -> verify(memberProfileRepository).findById(anyLong()),
                () -> verify(chattingRoomRepository).findById(anyLong()),
                () -> verify(chattingRoomRepository).save(any(ChattingRoom.class))
        );
    }

    @Test
    void 채팅_메시지_전송_실패_회원_없음() {
        // given
        willThrow(new NotFoundException(ApplicationError.MEMBER_NOT_FOUND)).given(memberProfileRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> chattingService.sendMessage(ID, chattingRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.MEMBER_NOT_FOUND.getMessage());
    }

    @Test
    void 채팅_메시지_전송_실패_채팅방_없음() {
        // given
        willReturn(Optional.of(memberProfile)).given(memberProfileRepository).findById(anyLong());
        willThrow(new NotFoundException(ApplicationError.CHATTING_ROOM_NOT_FOUND)).given(chattingRoomRepository).findById(anyLong());
        // when & then
        assertThatThrownBy(() -> chattingService.sendMessage(ID, chattingRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(ApplicationError.CHATTING_ROOM_NOT_FOUND.getMessage());
    }
}
