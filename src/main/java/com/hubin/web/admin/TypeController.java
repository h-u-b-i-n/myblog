package com.hubin.web.admin;


import com.hubin.entity.Type;
import com.hubin.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private TypeService  typeService;
    @GetMapping("/types")
    public String types(
            @PageableDefault(size=5,sort = {"id"},direction = Sort.Direction.DESC)  //用注解指定分页数的一些默认数据,10条数据按id倒叙排
            Pageable pageable, Model model){
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type",new Type());
        return "admin/types-input";

    }
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model){
        model.addAttribute("type",typeService.getType(id));
        return  "admin/types-input";

    }


    @PostMapping("/types")              //是post 和上面的get类型不同  不会冲突的
    public String post(@Valid  Type type, BindingResult result, RedirectAttributes attributes){
//校验的
        Type type1=typeService.getTypeByName(type.getName());
        if(type1!=null){
            result.rejectValue("name","nameError","不能添加重复分类");
        }
        //@Valid将后端校验传到前端    result可以接受校验后的结果
        if(result.hasErrors()){
            return  "/admin/types-input";
        }
        Type t=typeService.saveType(type);
        if(t==null){
           attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/types";
    }



    @PostMapping("/types/{id}")
    public String editPost(@Valid  Type type, BindingResult result,@PathVariable Long id, RedirectAttributes attributes){
//校验的
        Type type1=typeService.getTypeByName(type.getName());
        if(type1!=null){
            result.rejectValue("name","nameError","不能添加重复分类");
        }
        //@Valid将后端校验传到前端    result可以接受校验后的结果
        if(result.hasErrors()){
            return  "/admin/types-input";
        }
        Type t=typeService.updateType(id,type);
        if(t==null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/types";
    }


    @GetMapping("types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes attributes){
        typeService.deleteType(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";

    }
}


