package com.hubin.web;

import com.hubin.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ArchivesController {
    @Autowired
    private BlogService blogService;
    @GetMapping("/archives")
    public String archives(Model model){             //按年龄分组，里面放blogs
        model.addAttribute("archiveMap",blogService.archiveBlog());
        model.addAttribute("blogCount",blogService.countBlog());
        return "archives";
    }
}
