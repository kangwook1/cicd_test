package com.appcenter.practice.domain;


import com.appcenter.practice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    //삭제 여부에 따라 "삭제된 댓글입니다." 작성
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_Id",nullable = false)
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "todo_Id",nullable = false)
    private Todo todo;


    @Builder
    private Comment(String content,Boolean deleted, Member member, Todo todo) {
        this.content = content;
        this.deleted = deleted;
        this.member = member;
        this.todo = todo;
    }

    public void changeDeleted(Boolean deleted){ this.deleted=deleted;}
    public void changeContent(String content){ this.content=content; }
}
