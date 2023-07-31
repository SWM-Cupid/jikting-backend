package com.cupid.jikting.member.repository;

import com.cupid.jikting.common.entity.Hobby;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class HobbyRepositoryTest {

    private static final String KEYWORD = "취미 키워드";

    @Autowired
    private HobbyRepository hobbyRepository;

    @BeforeEach
    void setUp() {
        hobbyRepository.save(
                Hobby.builder()
                        .keyword(KEYWORD)
                        .build());
    }

    @Test
    void 키워드로_취미_조회_성공() {
        // when
        Hobby hobby = hobbyRepository.findByKeyword(KEYWORD)
                .orElseThrow(() -> new NotFoundException(ApplicationError.HOBBY_NOT_FOUND));
        // then
        assertThat(hobby.getKeyword()).isEqualTo(KEYWORD);
    }
}
