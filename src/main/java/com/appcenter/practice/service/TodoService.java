package com.appcenter.practice.service;


import com.appcenter.practice.domain.Bucket;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.request.todo.AddTodoReq;
import com.appcenter.practice.dto.request.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.todo.TodoRes;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.repository.BucketRepository;
import com.appcenter.practice.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.appcenter.practice.common.StatusCode.BUCKET_NOT_EXIST;
import static com.appcenter.practice.common.StatusCode.TODO_NOT_EXIST;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final BucketRepository bucketRepository;


    public List<TodoRes> getTodoList(Long bucketId){
        Bucket bucket=findByBucketId(bucketId);
        return  bucket.getTodoList().stream()
                .map(TodoRes::from)
                .collect(Collectors.toList());
    }



    @Transactional
    public TodoRes saveTodo(Long bucketId, AddTodoReq reqDto){
        Bucket bucket=findByBucketId(bucketId);
        Todo todo =todoRepository.save(reqDto.toEntity(bucket));
        return TodoRes.from(todo);
    }

    @Transactional
    public TodoRes updateTodo(Long todoId, UpdateTodoReq reqDto){
        Todo todo =findByTodoId(todoId);
        todo.changeContent(reqDto.getContent());

        return TodoRes.from(todo);
    }

    @Transactional
    public TodoRes completeTodo(Long todoId){
        Todo todo=findByTodoId(todoId);
        todo.changeCompleted();

        return TodoRes.from(todo);
    }

    @Transactional
    public void deleteTodo(Long todoId){
        Todo todo =findByTodoId(todoId);
        todoRepository.deleteById(todo.getId());
    }

    private Todo findByTodoId(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(()->new CustomException(TODO_NOT_EXIST));
    }

    private Bucket findByBucketId(Long bucketId){
        return bucketRepository.findById(bucketId)
                .orElseThrow(()->new CustomException(BUCKET_NOT_EXIST));
    }

}
