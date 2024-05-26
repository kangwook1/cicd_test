package com.appcenter.practice.swagger;

import com.appcenter.practice.dto.response.CommonResponse;
import com.appcenter.practice.dto.response.todo.TodoRes;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "투두 응답 DTO")
public class CommonResponseTodoRes extends CommonResponse<TodoRes> {

}