package kr.carrot.springwebmvc.servlet.basic;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printStartLine(request);
        printHeaders(request);
        printHeaderUtils(request);
        printEtc(request);

        response.getWriter().write("ok");
    }

    private void printStartLine(HttpServletRequest request) {
        System.out.println("========================================================");

        System.out.println("RequestHeaderServlet.printStartLine");

        System.out.println("request.getMethod() = " + request.getMethod());
        System.out.println("request.getProtocol() = " + request.getProtocol());
        System.out.println("request.getScheme() = " + request.getScheme());
        System.out.println("request.getRequestURL() = " + request.getRequestURL());
        System.out.println("request.getQueryString() = " + request.getQueryString());
        System.out.println("request.isSecure() = " + request.isSecure());

        System.out.println("========================================================");
    }

    private void printHeaders(HttpServletRequest request) {
        System.out.println("==========================================================");

        System.out.println("RequestHeaderServlet.printHeaders");

        request.getHeaderNames().asIterator()
                .forEachRemaining(headerName -> System.out.println("request = " + request.getHeader(headerName)));

        System.out.println("==========================================================");
    }


    private void printHeaderUtils(HttpServletRequest request) {
        System.out.println("=====================================================");

        System.out.println("RequestHeaderServlet.printHeaderUtils");

        System.out.println("request.getServerName() = " + request.getServerName());
        System.out.println("request.getServerPort() = " + request.getServerPort());
        System.out.println("request.getContentType() = " + request.getContentType());

        request.getLocales().asIterator()
                .forEachRemaining(locale -> System.out.println("locale = " + locale));
        System.out.println("request.getLocale() = " + request.getLocale());

        System.out.println("=====================================================");
    }

    private void printEtc(HttpServletRequest request) {
        System.out.println("======================================================");

        System.out.println("RequestHeaderServlet.printEtc");

        System.out.println("request.getRemoteHost() = " + request.getRemoteHost());
        System.out.println("request.getRemoteAddr() = " + request.getRemoteAddr());
        System.out.println("request = " + request.getRemotePort());

        System.out.println("request.getLocalName() = " + request.getLocalName());
        System.out.println("request.getLocalAddr() = " + request.getLocalAddr());
        System.out.println("request.getLocalPort() = " + request.getLocalPort());

        System.out.println("======================================================");
    }
}
