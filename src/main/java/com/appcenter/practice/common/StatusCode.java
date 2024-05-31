package com.appcenter.practice.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import static org.springframework.http.HttpStatus.*;

@Getter
@RequiredArgsConstructor
public enum StatusCode {

    /* 2xx: 성공 */
    // Member
    MEMBER_CREATE(CREATED,"회원 가입 완료"),
    MEMBER_FOUND(OK,"멤버 조회 완료"),
    MEMBER_UPDATE(OK,"멤버 수정 완료"),
    MEMBER_DELETE(OK,"회원탈퇴 완료"),
    MEMBER_LOGIN(OK,"로그인 완료"),
    // BucketList
    BUCKET_CREATE(CREATED,"버킷 생성 완료"),
    BUCKET_LIST_FOUND(OK,"버킷 목록 조회 완료"),
    BUCKET_UPDATE(OK,"버킷 수정 완료"),
    BUCKET_COMPLETE(OK,"버킷 상태 토글 완료"),
    BUCKET_DELETE(OK,"버킷 삭제 완료"),
    // TodoList
    TODO_CREATE(CREATED,"할 일 생성 완료"),
    TODO_LIST_FOUND(OK,"할 일 목록 조회 완료"),
    TODO_UPDATE(OK,"할 일 수정 완료"),
    TODO_COMPLETE(OK,"할 일 상태 토글 완료"),
    TODO_DELETE(OK,"할 일 삭제 완료"),
    // Comment
    COMMENT_CREATE(CREATED,"댓글 생성 완료"),
    COMMENT_LIST_FOUND(OK,"댓글 목록 조회 완료"),
    COMMENT_UPDATE(OK,"댓글 수정 완료"),
    COMMENT_DELETE(OK,"댓글 삭제 완료"),

    /* 400 BAD_REQUEST : 잘못된 요청 */
    LOGIN_ID_INVALID(BAD_REQUEST,"아이디가 틀렸습니다."),
    PASSWORD_INVALID(BAD_REQUEST,"비밀번호가 틀렸습니다."),
    INPUT_VALUE_INVALID(BAD_REQUEST,"유효하지 않은 입력입니다."),
    DATE_FORMAT_INVALID(BAD_REQUEST,"유효하지 않은 날짜 형식입니다."),

    /* 401 UNAUTHORIZED : 비인증 사용자 */
    ACCESS_TOKEN_INVALID(UNAUTHORIZED,"jwt 토큰이 유효하지 않습니다."),

    /* 403 FORBIDDEN : 권한 없음 */

    /* 404 NOT_FOUNT : 존재하지 않는 리소스 */
    MEMBER_NOT_EXIST(NOT_FOUND,"존재하지 않는 멤버입니다."),
    BUCKET_NOT_EXIST(NOT_FOUND,"존재하지 않는 버킷입니다."),
    TODO_NOT_EXIST(NOT_FOUND,"존재하지 않는 할 일입니다."),
    COMMENT_NOT_EXIST(NOT_FOUND,"존재하지 않는 댓글입니다."),

    /* 409 CONFLICT : 리소스 충돌 */
    EMAIL_DUPLICATED(CONFLICT,"이미 존재하는 이메일입니다.");


    private final HttpStatus status;
    private final String message;
}
