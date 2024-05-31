package com.appcenter.practice.swagger;

import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.bucket.BucketRes;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "버킷 응답 DTO")
public class CommonResponseBucketRes extends CommonResponse<BucketRes> {
}
