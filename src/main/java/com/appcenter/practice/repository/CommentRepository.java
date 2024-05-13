package com.appcenter.practice.repository;

import com.appcenter.practice.domain.Comment;
import com.appcenter.practice.domain.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findAllBytodo(Long todoId);
}
