package com.cupid.jikting.chatting.repository;

import com.cupid.jikting.chatting.entity.Chatting;
import com.cupid.jikting.chatting.entity.ChattingRoom;
import com.cupid.jikting.member.entity.MemberProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ChattingRoomRepositoryTest {

    private static final String CONTENT = "내용";

    @Autowired
    private ChattingRoomRepository chattingRoomRepository;

    @Test
    void 채팅방에_채팅_저장_성공() {
        // given
        MemberProfile memberProfile = MemberProfile.builder()
                .build();
        ChattingRoom chattingRoom = ChattingRoom.builder().build();
        Chatting chatting = Chatting.builder()
                .content(CONTENT)
                .memberProfile(memberProfile)
                .build();
        chattingRoom.createChatting(chatting);
        // when
        ChattingRoom savedChattingRoom = chattingRoomRepository.save(chattingRoom);
        // then
        assertThat(savedChattingRoom.getChattings().size()).isEqualTo(chattingRoom.getChattings().size());
    }
}
