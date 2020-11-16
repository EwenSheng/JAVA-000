package org.home.work.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @author: Ewen
 * @program: JAVA-000
 * @date: 2020/11/15 21:10
 * @description: test
 * <p>
 * 1、标准写法
 * 权限修饰符 返回值类型 包名.包名.包名..包名.类名.方法名(参数)
 * <p>
 * 2、省略权限修饰符
 * 返回值类型 包名.包名.包名..包名.类名.方法名(参数)
 * <p>
 * 3、返回值可使用通配符代替，表示任意类型
 * * 包名.包名.包名..包名.类名.方法名(参数)
 * <p>
 * 4、包名可使用通配符表示任意包名
 * 1）* *.*.*.*.类名.方法名(参数)
 * 2）* *..*.类名.方法名(参数)
 * <p>
 * 5、参数列表
 * 基本类型直接写名称 int
 * 引用类型写包名.类名形式 java.lang.String
 * 使用通配符*表示有参数
 * 使用..表示有无参数均可
 * <p>
 * 6、全通配
 * * *..*.*.*(..)
 */
@Aspect
@Component
public class AopConfig {

    @Pointcut("execution(* org.home.work.service.*.*(..))")
    public void method() {

    }


    @Before("method()")
    public void before() {
        System.out.println("前置通知....");
    }

    @After("method()")
    public void AfterReturning() {
        System.out.println("后置通知....");
    }


    @Around("method()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {

        System.out.println("环绕通知前....");

        Object obj = joinPoint.proceed();

        System.out.println("环绕通知后....");

        return obj;
    }

}
