package com.hubin.service;

import com.hubin.entity.Blog;
import com.hubin.vo.BlogQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface BlogService {
    Blog getAndConvert(Long id);
    Blog getBlog(Long id);
    Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery);
    Page<Blog>  listBlog(Pageable pageable);
    Page<Blog>  listBlog(String query,Pageable pageable);//定义一个字符串的查询方法
    Page<Blog> listBlog(Long tagId,Pageable pageable);
    Blog saveBlog(Blog blog);
    Map<String,List<Blog>> archiveBlog();
    List<Blog>  listRecommendBlogTop(Integer size);
    Blog updateBlog(Long id,Blog blog);
    void deleteBlog(Long id);
    Long countBlog();
}
