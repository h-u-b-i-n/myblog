package com.hubin.web.admin;


import com.hubin.entity.Blog;
import com.hubin.entity.User;
import com.hubin.service.BlogService;
import com.hubin.service.TagService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class BlogController {
    private static final String INPUT="admin/blogs-input";
    private static final  String LIST="admin/blogs";
    private static  final String REDIRECT_LIST="redirect:/admin/blogs";

    @Autowired
    BlogService blogService;
    @Autowired
    TypeService typeService;
    @Autowired
    private TagService tagService;

    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size=8,sort={"updateTime"},direction = Sort.Direction.DESC)
                                    Pageable pageable, BlogQuery blogQuery, Model model){


        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        return LIST;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size=8,sort={"updateTime"},direction = Sort.Direction.DESC)
                         Pageable pageable,BlogQuery blogQuery,Model model
                         ){
        model.addAttribute("page",blogService.listBlog(pageable, blogQuery));
        return  "admin/blogs :: blogList";       //响应ajax请求（给blogList）

    }


    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeAndTag(model);
        model.addAttribute("blog", new Blog());
        return INPUT;
    }

    private void setTypeAndTag(Model model) {
        model.addAttribute("types", typeService.listType());
        model.addAttribute("tags", tagService.listTag());
    }


    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model) {
        setTypeAndTag(model);
        Blog blog = blogService.getBlog(id);
        //先将获得对象里的tagids从集合格式装换成字符串格式  再传到前端
        blog.init();
        model.addAttribute("blog",blog);
        return INPUT;
    }



    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));  //根据前面编辑时的一些选择（类型，标签）
        //提取出来（前端 对blog new了一个Type，Type里set了一个id）  对blog对象进行一些初始化
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null) {
            b =  blogService.saveBlog(blog);
        } else {
            b = blogService.updateBlog(blog.getId(), blog);
        }

        if (b == null ) {
            attributes.addFlashAttribute("message", "操作失败");
        } else {
            attributes.addFlashAttribute("message", "操作成功");
        }
        return REDIRECT_LIST;
    }


    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes) {
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message", "删除成功");
        return REDIRECT_LIST;
    }


}

