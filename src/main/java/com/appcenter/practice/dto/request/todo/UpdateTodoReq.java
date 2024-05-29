package com.appcenter.practice.dto.request.todo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;

@Schema(description = "투두 변경 요청 DTO")
@Getter
public class UpdateTodoReq {

    @Schema(title = "내용",description = "필수 입력",
            example = "오픽하기")
    @NotBlank(message = "content는 필수 입력값입니다.")
    String content;

    @Schema(title = "마감 기한",description = "필수 입력, (yyyy-MM-dd)형태로 작성",
            example = "2024-05-25")
    private LocalDate deadLine;
}
