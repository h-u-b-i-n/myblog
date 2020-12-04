package com.hubin.handler;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerExceptionHandler {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView handler(HttpServletRequest request, Exception e) throws  Exception{
        //控制日志打印一些东西  以占位的方式传值 info  warn  debug同理
        //e.getMessage() 就不会将异常信息打印到控制台和日志文件中
        logger.error("Request URL : {},Exception : {}",request.getRequestURL(),e);
        //判断异常是否自定义被指定，如果被指定则不为空，抛出被指定异常  交给springboot处理
        if (AnnotationUtils.findAnnotation(e.getClass(),
                ResponseStatus.class) != null) {
            throw e;
        }
        ModelAndView mv=new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");
        return  mv;
    }
}
