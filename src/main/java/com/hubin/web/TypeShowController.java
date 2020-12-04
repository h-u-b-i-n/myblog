package com.hubin.web;

import com.hubin.entity.Type;
import com.hubin.service.BlogService;
import com.hubin.service.TypeService;
import com.hubin.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TypeShowController {
    @Autowired
    private TypeService typeService;
    @Autowired
    private BlogService blogService;
    @GetMapping("/types/{id}")    //当前选中状态的type的id
    public String types(@PageableDefault(size=8,sort={"updateTime"},direction = Sort.Direction.DESC)
                                    Pageable pageable, @PathVariable Long id, Model model
            , HttpServletRequest request){

        System.out.println(request.getRequestURL());
        List<Type> types=typeService.listTypeTop(10000);  //指定查询的size ，设置的大一点 就都能查到
        if(id==-1){      //说明是从首页点过来的     要展示的是types列表中的第一个
            id=types.get(0).getId();
        }
        BlogQuery blogQuery=new BlogQuery();
        blogQuery.setTypeId(id);
        model.addAttribute("types",types);
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));

        model.addAttribute("activeTypeId",id);
        return "types";
    }

}
