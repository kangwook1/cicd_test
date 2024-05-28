package com.appcenter.practice.dto.request.member;

import com.appcenter.practice.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;


@Schema(description = "로그인 요청 DTO")
@Getter
public class LoginMemberReq {

    @Schema(title = "이메일",description = "이메일 형식에 맞게 입력",
            example = "example@naver.com")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    private String email;

    @Schema(title = "비밀번호",description = "8자 이상의 영어, 숫자, @,$,!,%,*,#,?,& 중 하나 이상을 포함한 특수문자로 입력",
            example = "example123!!")
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    @Pattern(message = "비밀번호는 8자 이상의 영어, 숫자, @,$,!,%,*,#,?,& 중 하나 이상을 포함한 특수문자로 이루어져야 합니다."
            , regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$")
    private String password;

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .build();
    }
}

