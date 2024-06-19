package com.appcenter.practice.service;


import com.appcenter.practice.common.StatusCode;
import com.appcenter.practice.domain.Comment;
import com.appcenter.practice.domain.Member;
import com.appcenter.practice.domain.Todo;
import com.appcenter.practice.dto.request.comment.AddCommentReq;
import com.appcenter.practice.dto.request.comment.UpdateCommentReq;
import com.appcenter.practice.dto.response.comment.CommentRes;
import com.appcenter.practice.exception.CustomException;
import com.appcenter.practice.repository.CommentRepository;
import com.appcenter.practice.repository.MemberRepository;
import com.appcenter.practice.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.appcenter.practice.common.StatusCode.*;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;
    private final MemberRepository memberRepository;


    public List<CommentRes> getCommentList(Long todoId){
        Todo todo =findByTodoId(todoId);
        List<CommentRes> commentResList= todo.getCommentList().stream()
                .map(CommentRes::from)
                .toList();
        for(CommentRes commentRes : commentResList){
            if(commentRes.getDeleted())
                commentRes.setContent("삭제된 댓글입니다.");
        }
        return commentResList;
    }

//    public ReadCommentRes getComment(Long id){
//        Comment comment=findByCommentId(id);
//        return ReadCommentRes.from(comment);
//    }

    @Transactional
    public CommentRes saveComment(Long memberId, Long todoId, AddCommentReq reqDto){
        Member member=findByMemberId(memberId);
        Todo todo = findByTodoId(todoId);
        Comment comment=commentRepository.save(reqDto.toEntity(member, todo));
        return CommentRes.from(comment);
    }

    @Transactional
    public CommentRes updateComment(Long memberId, Long commentId, UpdateCommentReq reqDto){
        Member member=findByMemberId(memberId);
        Comment comment= findByCommentId(commentId);
        if(member.equals(comment.getMember())){
            comment.changeContent(reqDto.getContent());
            return CommentRes.from(comment);
        }
        else throw new CustomException(AUTHORIZATION_INVALID);



    }

    @Transactional
    public void deleteComment(Long memberId,Long commentId){
        Member member=findByMemberId(memberId);
        Comment comment=findByCommentId(commentId);
        if(member.equals(comment.getMember()))
            comment.changeDeleted(true);
        else throw new CustomException(AUTHORIZATION_INVALID);
    }

    private Comment findByCommentId(Long id){
       return commentRepository.findById(id)
                .orElseThrow(()->new CustomException(StatusCode.COMMENT_NOT_EXIST));
    }

    private Todo findByTodoId(Long id){
        return todoRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.TODO_NOT_EXIST));
    }

    private Member findByMemberId(Long id){
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(StatusCode.MEMBER_NOT_EXIST));
    }

}
