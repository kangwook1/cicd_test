package com.appcenter.practice.dto.response.member;

import com.appcenter.practice.domain.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "멤버 응답 DTO")
@Getter
public class MemberRes {

    @Schema(title = "멤버 Id", description = "멤버 Id", example = "1")
    private final Long id;

    @Schema(title = "이메일", description = "이메일", example = "example@naver.com")
    private final String email;

    @Schema(title = "닉네임", description = "닉네임", example = "냄B뚜껑")
    private final String nickname;

    @Schema(title = "프로필", description = "프로필 이미지", example = "basic.jpg")
    private final String profile;

    @Builder
    public MemberRes(Long id, String email, String nickname, String profile) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.profile= profile;
    }

    public static MemberRes from(Member member){
        return MemberRes.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .profile(member.getProfile())
                .build();
    }
}
