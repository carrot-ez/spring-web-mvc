package kr.carrot.springwebmvc.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("ResponseHeaderServlet.service");

        // Status code
        response.setStatus(HttpServletResponse.SC_OK);

//        response.setHeader("Content-Type", "text/plain; charset=utf-8");
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");

        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("my-header", "hello");

        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600); // 600 sec
        response.addCookie(cookie);

        // redirect
        // response.sendRedirect("/");

        response.getWriter().println("ok");
    }
}
