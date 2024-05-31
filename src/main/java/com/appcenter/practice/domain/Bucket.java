package com.appcenter.practice.domain;


import com.appcenter.practice.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Bucket extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDate deadLine;

    // boolean은 null값을 가질 수 없다. 기본적으로 false값을 가진다. Boolean 클래스 객체는 null값을 가질 수 있다.
    @Column(nullable = false)
    private Boolean completed;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id",nullable = false)
    private Member member;

    @OneToMany(mappedBy = "bucket",cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Todo> todoList= new ArrayList<>();

    @Builder
    private Bucket(String content, LocalDate deadLine, Boolean completed, Member member) {
        this.content = content;
        this.deadLine= deadLine;
        this.completed = completed;
        this.member = member;
    }


    public void changeCompleted(){ this.completed= !this.completed; }
    public void changeContent(String content){
        this.content= content;
    }
}

