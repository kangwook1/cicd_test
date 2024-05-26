package com.appcenter.practice.service;


import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.reqeust.todo.AddTodoReq;
import com.appcenter.practice.dto.reqeust.todo.UpdateTodoReq;
import com.appcenter.practice.dto.response.todo.TodoRes;
import com.appcenter.practice.exception.CustomException;
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
    private final MemberRepository memberRepository;


    public List<TodoRes> getTodoList(Long memberId){
        Member member=findByMemberId(memberId);
        return  member.getTodoList().stream()
                .map(TodoRes::from)
                .collect(Collectors.toList());
    }

//    public TodoRes getTodo(Long id){
//        Todo todo=findByTodoId(id);
//        return TodoRes.from(todo);
//    }


    @Transactional
    public TodoRes saveTodo(Long jwtMemberId, Long memberId, AddTodoReq reqDto){
        if(jwtMemberId.equals(memberId)){
            Member member=findByMemberId(memberId);
            Todo todo=todoRepository.save(reqDto.toEntity(member));
            return TodoRes.from(todo);
        }
        else
            throw new CustomException(AUTHORIZATION_INVALID);
    }

    @Transactional
    public TodoRes updateTodo(Long memberId, Long todoId,UpdateTodoReq reqDto){
        Member member=findByMemberId(memberId);
        Todo todo=findByTodoId(todoId);
        if(checkAuthorization(member,todo))
            todo.changeContent(reqDto.getContent());

        return TodoRes.from(todo);
    }

    @Transactional
    public TodoRes completeTodo(Long memberId, Long todoId){
        Member member=findByMemberId(memberId);
        Todo todo=findByTodoId(todoId);
        if(checkAuthorization(member,todo))
            todo.changeCompleted();

        return TodoRes.from(todo);
    }

    @Transactional
    public Long deleteTodo(Long memberId, Long todoId){
        Member member=findByMemberId(memberId);
        Todo todo=findByTodoId(todoId);
        if(checkAuthorization(member,todo))
            todoRepository.deleteById(todoId);

        return todoId;
    }

    private Todo findByTodoId(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(()->new CustomException(TODO_NOT_EXIST));
    }

    private Member findByMemberId(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(MEMBER_NOT_EXIST));
    }

    private boolean checkAuthorization(Member member,Todo todo){
        if(member.getId().equals(todo.getMember().getId()))
            return true;
        else
            throw new CustomException(AUTHORIZATION_INVALID);
    }
}
