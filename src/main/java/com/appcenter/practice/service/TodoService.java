package com.appcenter.practice.service;


import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.request.todo.AddTodoReq;
import com.appcenter.practice.dto.request.todo.UpdateTodoReq;
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


    public List<TodoRes> getMyTodoList(Long memberId){
        Member member=findByMemberId(memberId);
        return  member.getTodoList().stream()
                .map(TodoRes::from)
                .collect(Collectors.toList());
    }

    public List<TodoRes> getOtherUserTodoList(String nickname){
        Member member=memberRepository.findByNickname(nickname)
                .orElseThrow(()-> new CustomException(MEMBER_NOT_EXIST));
        return  member.getTodoList().stream()
                .map(TodoRes::from)
                .collect(Collectors.toList());
    }



    @Transactional
    public TodoRes saveTodo(Long memberId, AddTodoReq reqDto){
        Member member=findByMemberId(memberId);
        Todo todo=todoRepository.save(reqDto.toEntity(member));
        return TodoRes.from(todo);
    }

    @Transactional
    public TodoRes updateTodo(Long todoId,UpdateTodoReq reqDto){
        Todo todo=findByTodoId(todoId);
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
        Todo todo=findByTodoId(todoId);
        todoRepository.deleteById(todo.getId());
    }

    private Todo findByTodoId(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(()->new CustomException(TODO_NOT_EXIST));
    }

    private Member findByMemberId(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(()->new CustomException(MEMBER_NOT_EXIST));
    }

}
