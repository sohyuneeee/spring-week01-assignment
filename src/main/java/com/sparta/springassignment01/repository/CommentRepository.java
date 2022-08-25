package com.sparta.springassignment01.repository;

import com.sparta.springassignment01.entity.Comment;
import com.sparta.springassignment01.entity.Member;
import com.sparta.springassignment01.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByPost(Post post);
    List<Comment> findByMember(Member member);

    void deleteByPostId(Long id);



}
