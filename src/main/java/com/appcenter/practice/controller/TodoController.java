package com.appcenter.practice.controller;


import com.appcenter.practice.dto.request.todo.AddTodoReq;
import com.appcenter.practice.dto.request.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.ErrorResponse;
import com.appcenter.practice.dto.response.todo.TodoRes;
import com.appcenter.practice.service.TodoService;
import com.appcenter.practice.swagger.CommonResponseTodoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.appcenter.practice.common.StatusCode.*;


@Tag(name = "Todo", description = "Todo API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;


    @Operation(summary = "투두 리스트 조회", description ="해당 버킷의 투두 리스트를 조회합니다.",
            parameters = @Parameter(name = "bucketId", description = "버킷 Id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 리스트 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponseTodoRes.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt 토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping
    public ResponseEntity<CommonResponse<List<TodoRes>>> getMyTodoList(@RequestParam Long bucketId){
        return ResponseEntity.ok(CommonResponse.from(TODO_LIST_FOUND.getMessage(), todoService.getTodoList(bucketId)));
    }


    @Operation(summary = "투두 생성", description = "투두를 생성합니다.",
            parameters = @Parameter(name = "bucketId", description = "버킷 Id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "투두 생성 성공",content= @Content(schema = @Schema(implementation = CommonResponseTodoRes.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 날짜 형식입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<CommonResponse<TodoRes>> addTodo(Principal principal, @RequestParam Long bucketId, @RequestBody @Valid AddTodoReq reqDto){
        Long memberId = Long.parseLong(principal.getName());
        return ResponseEntity
                .status(TODO_CREATE.getStatus())
                .body(CommonResponse.from(TODO_CREATE.getMessage(), todoService.saveTodo(memberId,bucketId,reqDto)));
    }

    @Operation(summary = "투두 수정", description = "투두를 수정합니다.",
            parameters = @Parameter(name = "todoId", description = "투두 Id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 수정 성공",content= @Content(schema = @Schema(implementation = CommonResponseTodoRes.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 날짜 형식입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping(value = "/{todoId}")
    public ResponseEntity<CommonResponse<TodoRes>> updateTodo(Principal principal,@PathVariable Long todoId, @RequestBody @Valid UpdateTodoReq reqDto){
        Long memberId = Long.parseLong(principal.getName());
        return ResponseEntity
                .ok(CommonResponse.from(TODO_UPDATE.getMessage(), todoService.updateTodo(memberId,todoId,reqDto)));
    }

    @Operation(summary = "투두 완료", description = "투두의 완료 상태가 토글됩니다.",
            parameters = @Parameter(name = "todoId", description = "투두 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 완료 상태 변경 성공",content= @Content(schema = @Schema(implementation = CommonResponseTodoRes.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping(value = "/{todoId}/complete")
    public ResponseEntity<CommonResponse<TodoRes>> completeTodo(Principal principal,@PathVariable Long todoId){
        Long memberId = Long.parseLong(principal.getName());
        return ResponseEntity
                .ok(CommonResponse.from(TODO_COMPLETE.getMessage(), todoService.completeTodo(memberId,todoId)));
    }

    @Operation(summary = "투두 삭제", description = "투두를 삭제합니다",
            parameters = @Parameter(name = "todoId", description = "투두 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 삭제 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "401", description = "유효하지 않은 jwt토큰입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping(value = "/{todoId}")
    public ResponseEntity<CommonResponse<Object>> deleteTodo(Principal principal,@PathVariable Long todoId){
        Long memberId = Long.parseLong(principal.getName());
        todoService.deleteTodo(memberId,todoId);
        return ResponseEntity
                .ok(CommonResponse.from(TODO_DELETE.getMessage()));
    }

}
