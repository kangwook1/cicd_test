package com.appcenter.practice.swagger;

import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.member.MemberRes;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "멤버 응답 DTO")
public class CommonResponseMemberRes extends CommonResponse<MemberRes> {
}
