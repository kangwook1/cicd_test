package com.appcenter.practice.dto.reqeust.todo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "투두 변경 요청 DTO")
@Getter
public class UpdateTodoReq {

    @Schema(title = "내용",description = "필수 입력",
            example = "오픽하기")
    @NotBlank(message = "content는 필수 입력값입니다.")
    String content;

}
