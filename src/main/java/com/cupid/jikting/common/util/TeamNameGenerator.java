package com.cupid.jikting.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TeamNameGenerator extends RandomGenerator {

    private static final String[] COLORS = {
            "하양", "검정", "빨강", "주황", "노랑", "초록", "파랑", "보라", "분홍"
    };
    private static final String[] ANIMALS = {
            "강아지", "고양이", "오리", "사자", "호랑이", "판다", "코끼리", "기린", "원숭이", "캥거루", "악어", "상어", "사슴", "토끼",
            "거북이", "여우", "쿼카", "앵무새", "다람쥐", "너구리", "알파카", "치타", "펭귄", "햄스터", "미어캣", "수달", "표범"
    };
    private static final int RANDOM_NUMBER_RANGE = 10000;

    public static String generate() {
        return COLORS[RANDOM.nextInt(COLORS.length)]
                + ANIMALS[RANDOM.nextInt(ANIMALS.length)]
                + String.format("%04d", RANDOM.nextInt(RANDOM_NUMBER_RANGE));
    }
}
