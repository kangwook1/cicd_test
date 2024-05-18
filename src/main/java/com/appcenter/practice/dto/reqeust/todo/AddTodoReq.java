package com.appcenter.practice.dto.reqeust.todo;

import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "투두 생성 요청 DTO")
@Getter
public class AddTodoReq {

    @Schema(title = "이메일",description = "이메일 형식에 맞게 입력",
            example = "example@naver.com")
    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @Schema(title = "내용",description = "필수 입력",
            example = "토익하기")
    @NotBlank(message = "content는 필수 입력 값입니다.")
    private String content;

    public Todo toEntity(Member member){
        return Todo.builder()
                .content(content)
                .completed(false)
                .member(member)
                .build();
    }
}
