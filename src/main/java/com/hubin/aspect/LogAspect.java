package com.hubin.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;


@Component
@Aspect
public class LogAspect {
    //拿到日志记录器
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 辅助作用
     */
    @Pointcut("execution(* com.hb.web.*.*(..))")
    public void log() {

    }

    //在目标方法执行之前加入功能：记录日志内容（请求url 访问者ip 调用方法classMethod 参数 返回内容）在方法调用之前
//joinPoint是要加入切面功能的业务方法\

    @Before("log()")
    public void doBefore(JoinPoint joinPoint) {

        logger.info("----------------------doBefore-----------------------------");
        //获取request
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //类.方法名
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
        RequestLog requestLog = new RequestLog(
                request.getRequestURL().toString(),
                request.getRemoteAddr(),
                classMethod,
                //方法参数
                joinPoint.getArgs()
        );
        logger.info("Request------------{}", requestLog);

    }


    //表示切入点    ，    returing：自定义的变量（表示目标方法的返回值时 名字要和通知方法中相应形参名相同）
    @AfterReturning(returning = "res", pointcut = "log()")
    public void doAfterReturing(Object res) {
        logger.info("Result : {}" + res);

    }


    //用一个内部类封装日志内容
    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        @Override
        public String toString() {
            return "RequestLog{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }

        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }
    }
}
