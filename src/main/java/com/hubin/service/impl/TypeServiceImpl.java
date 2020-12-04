package com.hubin.service.impl;

import com.hubin.dao.TypeRepository;
import com.hubin.entity.Type;
import com.hubin.handler.NotFoundException;
import com.hubin.service.TypeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeRepository typeRepository;
    @Transactional       //增删改查要加事务
    @Override
    public Type saveType(Type type) {
        return typeRepository
.save(type);
    }
    @Transactional
    @Override
    public Type getType(Long id) {

        return typeRepository
.findOne(id);
    }

    @Transactional
    @Override
    public Type getTypeByName(String name) {
        return typeRepository
.findByName(name);
    }

    @Transactional
    @Override
    public List<Type> listTypeTop(Integer size) {
        Sort sort =new Sort(Sort.Direction.DESC,"blogs.size");//创造排序对象 从大到小 按照blog的size
        Pageable pageable=new PageRequest(0,size,sort);    //只要第一页
        return typeRepository.findTop(pageable);   //pageable表示的是分页方法
    }

    @Transactional
    @Override
    public Page<Type> listType(Pageable pageable)
    {
        return typeRepository.findAll(pageable);
    }

    @Transactional
    @Override
    public List<Type> listType() {
        return typeRepository
.findAll();
    }

    @Transactional
    @Override
    public Type updateType(long id, Type type) {

        Type t=typeRepository
.findOne(id);
        if(t==null){
            throw  new NotFoundException("不存在该类型");
        }
        BeanUtils.copyProperties(type,t);//将type里的值赋到t中
        return    typeRepository.save(t);  //save底层是insert；
    }
    @Transactional
    @Override
    public void deleteType(Long id) {

typeRepository.delete(id);
    }
}
