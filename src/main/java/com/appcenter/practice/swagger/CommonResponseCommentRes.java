package com.appcenter.practice.swagger;


import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.comment.CommentRes;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "댓글 응답 DTO")
public class CommonResponseCommentRes extends CommonResponse<CommentRes> {

}
