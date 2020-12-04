package com.hubin.dao;

import com.hubin.entity.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {
    List<Comment> findCommentByBlogIdAndParentCommentNull(Long blogId, Sort sort);  //根据blogid来查询 并且父级为空

}
