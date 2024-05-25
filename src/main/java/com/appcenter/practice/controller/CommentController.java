package com.appcenter.practice.controller;


import com.appcenter.practice.dto.reqeust.comment.AddCommentReq;
import com.appcenter.practice.dto.reqeust.comment.UpdateCommentReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.ErrorResponse;
import com.appcenter.practice.dto.response.comment.ReadCommentRes;
import com.appcenter.practice.service.CommentService;
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

import java.util.List;

import static com.appcenter.practice.common.StatusCode.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class CommentController {
    private final CommentService commentService;

    @Operation(summary = "댓글 리스트 조회", description ="보여질 댓글 리스트의 투두 아이디를 입력하세요.",
            parameters = @Parameter(name = "todoId", description = "투두 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 리스트 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public ResponseEntity<CommonResponse<List<ReadCommentRes>>>getCommentList(@RequestParam Long todoId){
        return ResponseEntity
                .ok(CommonResponse.of(COMMENT_FOUND.getMessage(), commentService.getCommentList(todoId)));
    }


    @Operation(summary = "단일 댓글 조회", description ="댓글 아이디를 입력하세요.",
            parameters = @Parameter(name = "commentId", description = "댓글 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "/{commentId}")
    public ResponseEntity<CommonResponse<ReadCommentRes>>getComment(@PathVariable Long commentId){
        return ResponseEntity
                .ok(CommonResponse.of(COMMENT_FOUND.getMessage(), commentService.getComment(commentId)));
    }


    @Operation(summary = "댓글 생성", description ="댓글을 달 투두의 아이디를 입력하세요.",
            parameters = @Parameter(name = "todoId", description = "투두 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 생성 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<CommonResponse<Long>> addComment(@RequestParam Long todoId, @RequestBody @Valid AddCommentReq reqDto){
        return ResponseEntity
                .status(COMMENT_CREATE.getStatus())
                .body(CommonResponse.of(COMMENT_CREATE.getMessage(), commentService.saveComment(todoId,reqDto)));
    }

    @Operation(summary = "댓글 수정", description ="댓글 아이디를 입력하세요.",
            parameters = @Parameter(name = "commentId", description = "댓글 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping(value = "/{commentId}")
    public ResponseEntity<CommonResponse<Long>> updateComment(@PathVariable Long commentId, @RequestBody @Valid UpdateCommentReq reqDto){
        return ResponseEntity
                .ok(CommonResponse.of(COMMENT_UPDATE.getMessage(), commentService.updateComment(commentId,reqDto)));
    }

    @Operation(summary = "댓글 삭제", description ="댓글 아이디를 입력하세요.",
            parameters = @Parameter(name = "commentId", description = "댓글 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping(value = "/{commentId}")
    public ResponseEntity<CommonResponse<Long>> deleteComment(@PathVariable Long commentId){
        return ResponseEntity
                .ok(CommonResponse.of(COMMENT_DELETE.getMessage(), commentService.deleteComment(commentId)));
    }
}
