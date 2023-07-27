package com.cupid.jikting.team.repository;

import com.cupid.jikting.common.entity.Personality;
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
class PersonalityRepositoryTest {

    private static final String KEYWORD = "활발한";

    @Autowired
    private PersonalityRepository personalityRepository;

    @BeforeEach
    void setUp() {
        personalityRepository.save(
                Personality.builder()
                        .keyword(KEYWORD)
                        .build());
    }

    @Test
    void 키워드로_팀성격_조회_성공() {
        // when
        Personality personality = personalityRepository.findByKeyword(KEYWORD)
                .orElseThrow(() -> new NotFoundException(ApplicationError.PERSONALITY_NOT_FOUND));
        // then
        assertThat(personality.getKeyword()).isEqualTo(KEYWORD);
    }
}
