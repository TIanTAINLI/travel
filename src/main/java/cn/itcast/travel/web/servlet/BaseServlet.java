package cn.itcast.travel.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) {
        //完成方法分发
        //1. 获取请求路径
        String uri = req.getRequestURI();   // /travel/user/add
        System.out.println("请求uri: " + uri);
        //2. 获取方法名称
        String methodName = uri.substring(uri.lastIndexOf('/') + 1);
        System.out.println("方法名称: " + methodName);
        //3. 获取方法对象Method
        //谁调用我, 我代表谁
        System.out.println(this);//UserServlet的对象
        try {
            //getDeclaredMethod 忽略访问权限修饰符, 获取方法, 包括私有方法都可以获取到
            Method method = this.getClass().getMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //4. 执行方法
            //暴力反射
//            method.setAccessible(true);
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
