package com.cupid.jikting.team.repository;

import com.cupid.jikting.common.entity.Personality;
import com.cupid.jikting.common.error.ApplicationError;
import com.cupid.jikting.common.error.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonalityRepositoryTest {

    @Autowired
    private PersonalityRepository personalityRepository;

    @Test
    void 키워드로_팀성격_조회_성공() {
        // given
        String keyword = "활발한";
        // when
        Personality personality = personalityRepository.findByKeyword(keyword)
                .orElseThrow(() -> new NotFoundException(ApplicationError.PERSONALITY_NOT_FOUND));
        // then
        assertThat(personality.getKeyword()).isEqualTo(keyword);
    }
}
