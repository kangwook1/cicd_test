package com.appcenter.practice.dto.reqeust.todo;

import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Schema(description = "투두 생성 요청 DTO")
@Getter
public class AddTodoReq {

    @Schema(title = "내용",description = "필수 입력",
            example = "토익하기")
    @NotBlank(message = "content는 필수 입력 값입니다.")
    private String content;

    @Schema(title = "마감 기한",description = "필수 입력, (yyyy-MM-dd)형태로 작성",
            example = "2024-05-25")
    @NotNull(message = "마감일은 null일 수 없습니다.")
    private LocalDate deadLine;


    public Todo toEntity(Member member){
        return Todo.builder()
                .content(content)
                .deadLine(deadLine)
                .completed(false)
                .member(member)
                .build();
    }
}
