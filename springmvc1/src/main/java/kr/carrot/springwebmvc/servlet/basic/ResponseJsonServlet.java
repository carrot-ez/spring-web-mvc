package kr.carrot.springwebmvc.servlet.basic;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.carrot.springwebmvc.servlet.vo.HelloData;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8"); // application/json은 스펙상 utf-8을 사용하므로 중복 추가임. 잘못된 코드

        HelloData helloData = new HelloData();
        helloData.setAge(26);
        helloData.setUsername("carrot");

        String result = objectMapper.writeValueAsString(helloData);

        response.getWriter().write(result);
    }
}
