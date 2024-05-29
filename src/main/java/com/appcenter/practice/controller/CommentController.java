package com.appcenter.practice.controller;


import com.appcenter.practice.dto.request.comment.AddCommentReq;
import com.appcenter.practice.dto.request.comment.UpdateCommentReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.ErrorResponse;
import com.appcenter.practice.dto.response.comment.CommentRes;
import com.appcenter.practice.service.CommentService;
import com.appcenter.practice.swagger.CommonResponseCommentRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.appcenter.practice.common.StatusCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 리스트 조회", description ="댓글 리스트를 조회합니다.<br>"+
            "삭제된 댓글은 [삭제된 댓글입니다.]라고 보여집니다.",
            parameters = @Parameter(name = "todoId", description = "투두 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 리스트 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponseCommentRes.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt 토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public ResponseEntity<CommonResponse<List<CommentRes>>>getCommentList(@RequestParam Long todoId){
        return ResponseEntity
                .ok(CommonResponse.from(COMMENT_LIST_FOUND.getMessage(), commentService.getCommentList(todoId)));
    }

    @Operation(summary = "댓글 생성", description ="댓글을 생성합니다.",
            parameters = @Parameter(name = "todoId", description = "투두 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 생성 성공",content= @Content(schema = @Schema(implementation = CommonResponseCommentRes.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt 토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<CommonResponse<CommentRes>> addComment(Principal principal,@RequestParam Long todoId, @RequestBody @Valid AddCommentReq reqDto){
        Long memberId=Long.parseLong(principal.getName());
        return ResponseEntity
                .status(COMMENT_CREATE.getStatus())
                .body(CommonResponse.from(COMMENT_CREATE.getMessage(), commentService.saveComment(memberId,todoId,reqDto)));
    }

    @Operation(summary = "댓글 수정", description ="댓글을 수정합니다.",
            parameters = @Parameter(name = "commentId", description = "댓글 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공",content= @Content(schema = @Schema(implementation = CommonResponseCommentRes.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt 토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping(value = "/{commentId}")
    public ResponseEntity<CommonResponse<CommentRes>> updateComment(@PathVariable Long commentId, @RequestBody @Valid UpdateCommentReq reqDto){
        return ResponseEntity
                .ok(CommonResponse.from(COMMENT_UPDATE.getMessage(), commentService.updateComment(commentId,reqDto)));
    }

    @Operation(summary = "댓글 삭제", description = "댓글은 soft delete방식으로 삭제가 되므로, 실제로 삭제되진 않고 댓글의 필드 중에서 deleted=true가 됩니다.",
            parameters = @Parameter(name = "commentId", description = "댓글 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt 토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<CommonResponse<Object>> deleteComment(@PathVariable Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity
                .ok(CommonResponse.from(COMMENT_DELETE.getMessage()));
    }
}
