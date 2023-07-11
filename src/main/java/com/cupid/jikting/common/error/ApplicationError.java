package com.cupid.jikting.common.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationError {

    VERIFICATION_CODE_NOT_EQUAL(HttpStatus.BAD_REQUEST, "C001", "인증번호가 일치하지 않습니다."),
    INVALID_FORMAT(HttpStatus.BAD_REQUEST, "C002", "잘못된 양식입니다."),
    INVALID_AUTHORITY(HttpStatus.BAD_REQUEST, "C003", "잘못된 권한입니다."),
    INVALID_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "C004", "지원하지 않는 파일 확장자입니다."),
    INVALID_FILE_SIZE(HttpStatus.BAD_REQUEST, "C005", "지원하지 않는 파일 크기입니다."),
    INCOMPLETE_FORM(HttpStatus.BAD_REQUEST, "C006", "채워지지 않은 내용이 있습니다."),
    POINT_NOT_ENOUGH(HttpStatus.BAD_REQUEST, "C007", "포인트가 부족합니다."),
    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST, "C008", "인증번호가 유효하지 않습니다."),

    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "U001", "인증되지 않은 사용자입니다."),
    FORBIDDEN_MEMBER(HttpStatus.FORBIDDEN, "U002", "권한이 없는 사용자입니다."),
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "U003", "사용자를 찾을 수 없습니다."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "U004", "해당 아이디가 이미 존재합니다."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "U005", "해당 닉네임이 이미 존재합니다."),
    INVALID_COMPANY(HttpStatus.BAD_REQUEST, "U006", "지원하지 않는 회사입니다."),
    NOT_EQUAL_ID_OR_PASSWORD(HttpStatus.BAD_REQUEST, "U007", "아이디 또는 비밀번호가 일치하지 않습니다."),

    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST, "T001", "팀을 찾을 수 없습니다."),
    GENDER_MISMATCH(HttpStatus.BAD_REQUEST, "T002", "해당 성별은 팀에 참여할 수 없습니다."),
    ALREADY_IN_TEAM(HttpStatus.BAD_REQUEST, "T003", "이미 팀에 가입되었습니다."),
    TEAM_ALREADY_FULL(HttpStatus.BAD_REQUEST, "T004", "이미 팀 모집이 완료되었습니다."),

    MEETING_NOT_FOUND(HttpStatus.BAD_REQUEST, "M001", "미팅을 찾을 수 없습니다."),

    INSTANT_MEETING_NOT_FOUND(HttpStatus.BAD_REQUEST, "I001", "번개팅을 찾을 수 없습니다."),
    INSTANT_MEETING_ALREADY_FULL(HttpStatus.BAD_REQUEST, "I002", "이미 번개팅 모집이 완료되었습니다."),

    CHATTING_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "C001", "채팅방을 찾을 수 없습니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "S001", "서버 내부 에러가 발생했습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
