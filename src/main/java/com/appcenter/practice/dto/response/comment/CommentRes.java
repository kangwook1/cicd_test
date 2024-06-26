package com.appcenter.practice.dto.response.comment;


import com.appcenter.practice.domain.Comment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description= "댓글 응답 DTO")
@Getter
public class CommentRes {
    @Schema(title = "댓글 Id",description = "댓글 Id", example = "1")
    private final Long commentId;

    @Schema(title = "내용",description = "내용", example = "멋있어요")
    private String content;

    @Schema(title = "삭제 여부",description = "삭제 됐는지 체크", example = "false")
    private final Boolean deleted;

    @Schema(title = "닉네임",description = "댓글 쓴 사람의 닉네임", example = "냄B뚜껑")
    private final String nickname;

    @Schema(title = "멤버 아이디",description = "댓글 쓴 사람의 멤버 아이디", example = "1")
    private final Long memberId;

    @Schema(title = "프로필 이미지",description = "댓글 쓴 사람의 프로필", example = "basic.jpg")
    private final String profile;

    @Schema(title = "생성 시간",description = "댓글 생성 시간", example = "2024-05-26T14:25:07")
    private final LocalDateTime createdTime;

    @Schema(title = "수정 시간",description = "댓글 수정 시간", example = "2024-05-26T14:25:09")
    private final LocalDateTime modifiedTime;

    @Builder
    private CommentRes(Long commentId, String content, Boolean deleted, String nickname, Long memberId, String profile, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.commentId = commentId;
        this.content = content;
        this.deleted = deleted;
        this.nickname = nickname;
        this.memberId = memberId;
        this.profile = profile;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public static CommentRes from(Comment comment) {
        return CommentRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .deleted(comment.getDeleted())
                .nickname(comment.getMember().getNickname())
                .memberId(comment.getMember().getId())
                .profile(comment.getMember().getProfile())
                .createdTime(comment.getCreatedDate())
                .modifiedTime(comment.getModifiedDate())
                .build();
    }
}
