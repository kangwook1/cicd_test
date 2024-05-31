package com.appcenter.practice.dto.request.bucket;

import com.appcenter.practice.domain.Bucket;
import com.appcenter.practice.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Schema(description = "버킷 생성 요청 DTO")
@Getter
public class AddBucketReq {

    @Schema(title = "내용",description = "필수 입력",
            example = "교환학생 가기")
    @NotBlank(message = "content는 필수 입력 값입니다.")
    private String content;

    @Schema(title = "마감 기한",description = "필수 입력, (yyyy-MM-dd)형태로 작성",
            example = "2024-05-25")
    @NotNull(message = "마감일은 null일 수 없습니다.")
    private LocalDate deadLine;


    public Bucket toEntity(Member member){
        return Bucket.builder()
                .content(content)
                .deadLine(deadLine)
                .completed(false)
                .member(member)
                .build();
    }
}