package com.appcenter.practice.dto.response.todo;

import com.appcenter.practice.domain.Todo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description= "투두 응답 DTO")
@Getter
public class TodoRes {

    @Schema(title = "투두 아이디",description = "투두 아이디", example = "1")
    private final Long todoId;

    @Schema(title = "내용",description = "투두 내용", example = "오픽하기")
    private final String content;

    @Schema(title = "마감 기한",description = "투두 마감 기한", example = "2024-08-24")
    private final LocalDate deadLine;

    @Schema(title = "완료 여부",description = "완료 했는지 확인", example = "false")
    private final Boolean completed;

    @Schema(title = "생성 시간",description = "댓글 생성 시간", example = "2024-05-26T14:25:07.546")
    private final LocalDateTime createdTime;

    @Schema(title = "수정 시간",description = "댓글 수정 시간", example = "2024-05-26T14:25:09.846")
    private final LocalDateTime modifiedTime;



    private TodoRes(Long todoId, String content, LocalDate deadLine, Boolean completed, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.todoId = todoId;
        this.content = content;
        this.deadLine = deadLine;
        this.completed = completed;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public static TodoRes from(Todo todo){
        return new TodoRes(todo.getId(),todo.getContent(),todo.getDeadLine(),todo.getCompleted(),todo.getCreatedDate(),todo.getModifiedDate());
    }

}
