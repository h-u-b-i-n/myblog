package com.hubin.service.impl;

import com.hubin.dao.BlogRepository;
import com.hubin.entity.Blog;
import com.hubin.entity.Type;
import com.hubin.handler.NotFoundException;
import com.hubin.service.BlogService;
import com.hubin.util.MarkdownUtils;
import com.hubin.util.MyBeanUtils;
import com.hubin.vo.BlogQuery;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;


@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogRepository blogRepository;

    @Transactional
    @Override
    public Blog getBlog(Long id) {
        return  blogRepository.findOne(id) ;
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @Override
    public Long countBlog() {
        return blogRepository.count();
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {
        List<String> years=blogRepository.findGroupYear();
        Map<String,List<Blog>> map=new HashMap<>();
        for(String year:years){
            map.put(year,blogRepository.findByYear(year));
        }
        return map;
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {

        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join join=root.join("tags");
                return criteriaBuilder.equal(join.get("id"),tagId);
            }
        }, pageable);
    }

    @Transactional
    @Override
    public Blog getAndConvert(Long id) {   //将Markdown格式转成html格式（需要两个扩展——
        // 里面有表格，获取表格的一些东西            <h>标签生成html加id（默认不回加）   便于生成目录结构
        Blog blog=blogRepository.findOne(id);
        if(blog==null){
            throw new NotFoundException("博客不存在");

        }
        Blog b=new Blog();   //从新创造一个blog对象 并赋值当成一个拷贝来用   避免数据库被直接修改了
        BeanUtils.copyProperties(blog,b);
         String content=b.getContent();
        b.setContent(MarkdownUtils.markdownToHtmlExtensions(content));
        blogRepository.updateViews(id);

        return b;
    }
    @Transactional
    @Override
    public List<Blog> listRecommendBlogTop(Integer size) {
        Sort sort =new Sort(Sort.Direction.DESC,"updateTime");//创造排序对象 从大到小 按照blog的size
        Pageable pageable=new PageRequest(0,size,sort);    //只要第一页
        return blogRepository.findTop(pageable);
    }
    @Transactional
    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {

        return blogRepository.findByQuery(query,pageable);
    }

    @Transactional
    @Override
    public Page<Blog> listBlog(Pageable pageable, BlogQuery blogQuery) {          //根据两个条件来动态查询(springboot继承jpa封装了方法)
        //先BlogRepository继承JpaSpecificationExecutor<Blog>
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate>predicates =new ArrayList<>();
                if (!"".equals(blogQuery.getTitle()) &&blogQuery.getTitle()!= null) {
                    predicates.add(criteriaBuilder.like(root.<String>get("title"),"%"+blogQuery.getTitle()+"%"));

                }if(blogQuery.getTypeId()!=null){
                    predicates.add(criteriaBuilder.equal(root.<Type>get("type").get("id"),blogQuery.getTypeId()));
                }if(blogQuery.isRecommend()){
                    predicates.add(criteriaBuilder.equal(root.<Boolean>get("recommend"),blogQuery.isRecommend()));
                }
                criteriaQuery.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Transactional
    @Override
    public Blog saveBlog(Blog blog) {
        if(blog==null){

            blog.setCreatTime(new Date());
            blog.setUpdateTime(new Date());
            blog.setViews(0);
        }else{
            blog.setUpdateTime(new Date());

        }
        return blogRepository.save(blog);
    }

    @Transactional
    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b=blogRepository.findOne(id);
        if(b==null){
            throw  new NotFoundException("");
        }
        //将blog对象里属性赋值给b，过滤掉blog里本来就是空的一部分,否则blog中空的也会赋值给b
        BeanUtils.copyProperties(blog,b, MyBeanUtils.getNullPropertyNames(blog));//最后一个属性是一个数组（属性值是空的属性）
        b.setUpdateTime(new Date());
        return blogRepository.save(b);
    }


    @Transactional
    @Override
    public void deleteBlog(Long id) {
        blogRepository.delete(id);
    }
}
