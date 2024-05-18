package com.appcenter.practice.controller;

import com.appcenter.practice.dto.reqeust.member.SignupMemberReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.ErrorResponse;
import com.appcenter.practice.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.appcenter.practice.common.StatusCode.MEMBER_CREATE;


@Tag(name = "Member", description = "Member API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    private final MemberService memberService;


    @Operation(summary = "멤버 회원가입", description =
            "1. 모든 값은 비어있거나 null이면 유효성 검사에 실패합니다.<br>"+
            "2. email값은 중복이 허용되지 않습니다.<br>"+
            "3. 비밀번호는 8자 이상의 영어, 숫자, @,$,!,%,*,#,?,& 중 하나 이상을 포함한 특수문자로 이루어져야 합니다.<br>"+
            "4. 닉네임은 특수문자를 포함하지 않은 2~10자리여야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "멤버 생성 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<CommonResponse<Long>> signup( @RequestBody @Valid SignupMemberReq reqDto){
        return ResponseEntity
                .status(MEMBER_CREATE.getStatus())
                .body(CommonResponse.of(MEMBER_CREATE.getMessage(), memberService.signup(reqDto)));
    }

}
