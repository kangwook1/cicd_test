package com.appcenter.practice.controller;


import com.appcenter.practice.dto.reqeust.todo.AddTodoReq;
import com.appcenter.practice.dto.reqeust.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.ErrorResponse;
import com.appcenter.practice.dto.response.todo.ReadTodoRes;
import com.appcenter.practice.service.TodoService;
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

import java.util.List;

import static com.appcenter.practice.common.StatusCode.*;


@Tag(name = "Todo", description = "Todo API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;


    @Operation(summary = "투두 리스트 조회", description ="시큐리티 구현을 하지 않았으므로, 모든 멤버의 투두 리스트가 조회됩니다.")
    @ApiResponse(responseCode = "200", description = "투두 리스트 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class)))
    @GetMapping
    public ResponseEntity<CommonResponse<List<ReadTodoRes>>> getTodoList(){
        return ResponseEntity.ok(CommonResponse.of(TODO_FOUND.getMessage(), todoService.getTodoList()));
    }

    @Operation(summary = "단일 투두 조회", description = "투두 아이디를 입력하세요.",
            parameters = @Parameter(name = "todoId", description = "투두 Id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @GetMapping(value = "/{todoId}")
    public ResponseEntity<CommonResponse<ReadTodoRes>> getTodo(@PathVariable Long todoId){
        return ResponseEntity.ok(CommonResponse.of(TODO_FOUND.getMessage(),todoService.getTodo(todoId)));
    }

    @Operation(summary = "투두 생성", description = "시큐리티 구현을 하지 않았으므로, 요청 DTO에 email을 입력하세요.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "투두 생성 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<CommonResponse<Long>> addTodo(@RequestBody @Valid AddTodoReq reqDto){
        return ResponseEntity
                .status(TODO_CREATE.getStatus())
                .body(CommonResponse.of(TODO_CREATE.getMessage(),todoService.saveTodo(reqDto)));
    }

    @Operation(summary = "투두 수정", description = "투두 아이디를 입력하세요.",
            parameters = @Parameter(name = "todoId", description = "투두 Id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 수정 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping(value = "/{todoId}")
    public ResponseEntity<CommonResponse<Long>> updateTodo(@PathVariable Long todoId, @RequestBody @Valid UpdateTodoReq reqDto){
        return ResponseEntity
                .ok(CommonResponse.of(TODO_UPDATE.getMessage(),todoService.updateTodo(todoId,reqDto)));
    }

    @Operation(summary = "투두 완료", description = "투두 아이디를 입력하세요. 투두의 완료 상태가 토글됩니다.",
            parameters = @Parameter(name = "todoId", description = "투두 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 완료 상태 변경 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping(value = "/{todoId}/complete")
    public ResponseEntity<CommonResponse<Long>> completeTodo(@PathVariable Long todoId){
        return ResponseEntity
                .ok(CommonResponse.of(TODO_UPDATE.getMessage(),todoService.completeTodo(todoId)));
    }

    @Operation(summary = "투두 삭제", description = "투두 아이디를 입력하세요.",
            parameters = @Parameter(name = "todoId", description = "투두 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 삭제 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping(value = "/{todoId}")
    public ResponseEntity<CommonResponse<Long>> deleteTodo(@PathVariable Long todoId){
        return ResponseEntity
                .ok(CommonResponse.of(TODO_DELETE.getMessage(),todoService.deleteTodo(todoId)));
    }

}
