package com.appcenter.practice.service;


import com.appcenter.practice.domain.Bucket;
import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.request.todo.AddTodoReq;
import com.appcenter.practice.dto.request.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.todo.TodoRes;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.repository.BucketRepository;
import com.appcenter.practice.repository.MemberRepository;
import com.appcenter.practice.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.appcenter.practice.common.StatusCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TodoService {
    private final TodoRepository todoRepository;
    private final BucketRepository bucketRepository;
    private final MemberRepository memberRepository;


    public List<TodoRes> getTodoList(Long bucketId){
        Bucket bucket=findByBucketId(bucketId);
        return  bucket.getTodoList().stream()
                .map(TodoRes::from)
                .collect(Collectors.toList());
    }



    @Transactional
    public TodoRes saveTodo(Long memberId,Long bucketId, AddTodoReq reqDto){
        Member member=findByMemberId(memberId);
        Bucket bucket=findByBucketId(bucketId);
        if(member.equals(bucket.getMember())){
            Todo todo =todoRepository.save(reqDto.toEntity(bucket));
            return TodoRes.from(todo);
        }
        else throw new CustomException(AUTHORIZATION_INVALID);
    }

    @Transactional
    public TodoRes updateTodo(Long memberId, Long todoId, UpdateTodoReq reqDto){
        Member member=findByMemberId(memberId);
        Todo todo =findByTodoId(todoId);
        if(member.equals(todo.getBucket().getMember())){
            todo.changeContent(reqDto.getContent());
            return TodoRes.from(todo);
        }
        else throw new CustomException(AUTHORIZATION_INVALID);

    }

    @Transactional
    public TodoRes completeTodo(Long memberId, Long todoId){
        Member member=findByMemberId(memberId);
        Todo todo=findByTodoId(todoId);
        if(member.equals(todo.getBucket().getMember())){
            todo.changeCompleted();
            return TodoRes.from(todo);
        }
        else throw new CustomException(AUTHORIZATION_INVALID);
    }

    @Transactional
    public void deleteTodo(Long memberId, Long todoId){
        Member member=findByMemberId(memberId);
        Todo todo =findByTodoId(todoId);
        if(member.equals(todo.getBucket().getMember()))
            todoRepository.deleteById(todo.getId());
        else throw new CustomException(AUTHORIZATION_INVALID);
    }

    private Todo findByTodoId(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(()->new CustomException(TODO_NOT_EXIST));
    }

    private Bucket findByBucketId(Long bucketId){
        return bucketRepository.findById(bucketId)
                .orElseThrow(()->new CustomException(BUCKET_NOT_EXIST));
    }

    private Member findByMemberId(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(MEMBER_NOT_EXIST));
    }

}
