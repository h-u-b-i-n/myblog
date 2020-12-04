package com.hubin.dao;

import com.hubin.entity.Type;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TypeRepository  extends JpaRepository<Type,Long> {
    Type findByName(String name);
    @Query("select t from Type t")              //自定义查询  select t from  Type对象
    List<Type> findTop(Pageable pageable) ;  //分类中也有排序  分页大小作为我们要查询的条数
    //按类型从大到小排序 取其中前六个
}
