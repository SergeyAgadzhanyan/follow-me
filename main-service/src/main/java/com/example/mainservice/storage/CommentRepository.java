package com.example.mainservice.storage;

import com.example.mainservice.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByEventId(Long events, Pageable pageable);

}
