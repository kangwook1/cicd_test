package com.appcenter.practice.dto.response.bucket;

import com.appcenter.practice.domain.Bucket;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description= "버킷 응답 DTO")
@Getter
public class BucketRes {

    @Schema(title = "버킷 Id",description = "버킷 Id", example = "1")
    private final Long bucketId;

    @Schema(title = "내용",description = "버킷 내용", example = "유학가기")
    private final String content;

    @Schema(title = "마감 기한",description = "버킷 마감 기한", example = "2024-08-24")
    private final LocalDate deadLine;

    @Schema(title = "완료 여부",description = "완료 했는지 확인", example = "false")
    private final Boolean completed;

    @Schema(title = "멤버 아이디",description = "버킷을 만든 사람의 멤버 아이디", example = "1")
    private final Long memberId;

    @Schema(title = "생성 시간",description = "버킷 생성 시간", example = "2024-05-26T14:25:07")
    private final LocalDateTime createdTime;

    @Schema(title = "수정 시간",description = "버킷 수정 시간", example = "2024-05-26T14:25:09")
    private final LocalDateTime modifiedTime;

    @Builder
    private BucketRes(Long bucketId, String content, LocalDate deadLine, Boolean completed, Long memberId, LocalDateTime createdTime, LocalDateTime modifiedTime) {
        this.bucketId = bucketId;
        this.content = content;
        this.deadLine = deadLine;
        this.completed = completed;
        this.memberId=memberId;
        this.createdTime = createdTime;
        this.modifiedTime = modifiedTime;
    }

    public static BucketRes from(Bucket bucket){
        return BucketRes.builder()
                .bucketId(bucket.getId())
                .content(bucket.getContent())
                .deadLine(bucket.getDeadLine())
                .completed(bucket.getCompleted())
                .memberId(bucket.getMember().getId())
                .createdTime(bucket.getCreatedDate())
                .modifiedTime(bucket.getModifiedDate())
                .build();
    }

}

