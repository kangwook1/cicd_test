package com.appcenter.practice.dto.request.comment;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Schema(description = "댓글 수정 요청 DTO")
@Getter
public class UpdateCommentReq {

    @Schema(title = "내용",description = "필수 입력",
            example = "낫뱃")
    @NotBlank(message = "content는 필수 입력 값입니다.")
    String content;
}
