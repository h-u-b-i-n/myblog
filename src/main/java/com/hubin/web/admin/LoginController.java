package com.hubin.web.admin;

import com.hubin.entity.User;
import com.hubin.service.UserService;
import com.hubin.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    private UserService  userService;


    @GetMapping  //此时默认使用全局的"/admin"
    public  String loginPage(){
        return "admin/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        User user=userService.checkUser(username, MD5Utils.code(password));
        System.out.println(user);
        if(user!=null){
            user.setPassword(null); //不要把密码传到前面
            session.setAttribute("user",user);
            //跳转到admin/login.html(return:"redirect:/admin/login"表示跳转请求admin/login,视图解析器用不上)
            return "admin/index";
        }else{
            //不能用model.addAttribute("","");
            //重定向表示会重新发起一个请求，采用的是不同的request对象，即，上一个request域对象中村的数据，下一次请求无法获取了
            attributes.addFlashAttribute("message","用户名字或者密码错误");
            return "redirect:/admin";
        }

    }
    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.removeAttribute("user");
        //请求跳转
        return "redirect:/admin";
    }

}
