package com.appcenter.practice.dto.request.comment;


import com.appcenter.practice.domain.Comment;
import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Schema(description = "댓글 생성 요청 DTO")
@Getter
public class AddCommentReq {

    @Schema(title = "내용",description = "필수 입력",
            example = "굿잡")
    @NotBlank(message = "content는 필수 입력값입니다.")
    private String content;

    public Comment toEntity(Member member,Todo todo){
        return Comment.builder()
                .content(content)
                .deleted(false)
                .member(member)
                .todo(todo)
                .build();
    }
}
