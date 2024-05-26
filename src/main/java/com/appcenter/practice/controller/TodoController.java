package com.appcenter.practice.controller;


import com.appcenter.practice.dto.reqeust.todo.AddTodoReq;
import com.appcenter.practice.dto.reqeust.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.ErrorResponse;
import com.appcenter.practice.dto.response.todo.TodoRes;
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

import java.security.Principal;
import java.util.List;

import static com.appcenter.practice.common.StatusCode.*;


@Tag(name = "Todo", description = "Todo API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/todos")
public class TodoController {
    private final TodoService todoService;


    @Operation(summary = "투두 리스트 조회", description ="쿼리 스트링으로 입력한 멤버 아이디로 멤버의 투두 리스트를 조회합니다.<br>"+
            "다른 사람의 투두 리스트를 조회하고, 댓글을 달아야하기 때문에 jwt에 있는 토큰의 아이디로 조회하지 않습니다.",
            parameters = @Parameter(name = "memberId", description = "멤버 Id", example = "1"))
    @ApiResponse(responseCode = "200", description = "투두 리스트 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class)))
    @GetMapping
    public ResponseEntity<CommonResponse<List<TodoRes>>> getTodoList(@RequestParam Long memberId){
        return ResponseEntity.ok(CommonResponse.of(TODO_FOUND.getMessage(), todoService.getTodoList(memberId)));
    }

//    @Operation(summary = "단일 투두 조회", description = "투두 아이디를 통해 단일 투두를 조회합니다.",
//            parameters = @Parameter(name = "todoId", description = "투두 Id", example = "1"))
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "투두 조회 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
//            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
//    @GetMapping(value = "/{todoId}")
//    public ResponseEntity<CommonResponse<ReadTodoRes>> getTodo(@PathVariable Long todoId){
//        return ResponseEntity.ok(CommonResponse.of(TODO_FOUND.getMessage(),todoService.getTodo(todoId)));
//    }

    @Operation(summary = "투두 생성", description = "쿼리 스트링으로 입력한 memberId로 투두를 생성합니다.<br>"+
            "내부적으로 jwt의 멤버 아이디와 쿼리 스트링으로 받아온 멤버 아이디를 비교해 권한을 체크합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "투두 생성 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 날짜 형식입니다",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PostMapping
    public ResponseEntity<CommonResponse<TodoRes>> addTodo(Principal principal, @RequestParam Long memberId,@RequestBody @Valid AddTodoReq reqDto){
        Long jwtMemberId=Long.parseLong(principal.getName());
        return ResponseEntity
                .status(TODO_CREATE.getStatus())
                .body(CommonResponse.of(TODO_CREATE.getMessage(),todoService.saveTodo(jwtMemberId,memberId,reqDto)));
    }

    @Operation(summary = "투두 수정", description = "투두 아이디를 통해 투두를 수정합니다.<br>"+
            "내부적으로 jwt의 멤버 아이디와 투두의 멤버 아이디를 비교해 권한을 체크합니다.",
            parameters = @Parameter(name = "todoId", description = "투두 Id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 수정 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 입력입니다",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효하지 않은 날짜 형식입니다",content= @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 투두입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping(value = "/{todoId}")
    public ResponseEntity<CommonResponse<TodoRes>> updateTodo(Principal principal, @PathVariable Long todoId, @RequestBody @Valid UpdateTodoReq reqDto){
        Long memberId=Long.parseLong(principal.getName());
        return ResponseEntity
                .ok(CommonResponse.of(TODO_UPDATE.getMessage(),todoService.updateTodo(memberId,todoId,reqDto)));
    }

    @Operation(summary = "투두 완료", description = "투두 아이디를 통해 투두의 완료 상태가 토글됩니다.<br>"+
            "내부적으로 jwt의 멤버 아이디와 투두의 멤버 아이디를 비교해 권한을 체크합니다.",
            parameters = @Parameter(name = "todoId", description = "투두 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 완료 상태 변경 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @PatchMapping(value = "/{todoId}/complete")
    public ResponseEntity<CommonResponse<TodoRes>> completeTodo(Principal principal, @PathVariable Long todoId){
        Long memberId=Long.parseLong(principal.getName());
        return ResponseEntity
                .ok(CommonResponse.of(TODO_UPDATE.getMessage(),todoService.completeTodo(memberId,todoId)));
    }

    @Operation(summary = "투두 삭제", description = "투두 아이디를 통해 투두가 삭제됩니다.<br>"+
            "내부적으로 jwt의 멤버 아이디와 투두의 멤버 아이디를 비교해 권한을 체크합니다.<br>"+
            "삭제 시 투두 아이디만 반환됩니다.",
            parameters = @Parameter(name = "todoId", description = "투두 id", example = "1"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "투두 삭제 성공",content= @Content(schema = @Schema(implementation = CommonResponse.class))),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 멤버입니다.",content= @Content(schema = @Schema(implementation = ErrorResponse.class)))})
    @DeleteMapping(value = "/{todoId}")
    public ResponseEntity<CommonResponse<Long>> deleteTodo(Principal principal, @PathVariable Long todoId){
        Long memberId=Long.parseLong(principal.getName());
        return ResponseEntity
                .ok(CommonResponse.of(TODO_DELETE.getMessage(),todoService.deleteTodo(memberId,todoId)));
    }

}
