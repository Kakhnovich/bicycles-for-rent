package com.epam.jwd.rent.controller.filter;

import com.epam.jwd.rent.model.UserDto;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebFilter(urlPatterns = {UrlPatterns.LOGIN, UrlPatterns.SIGN_UP}, initParams = {
        @WebInitParam(name = "page", value = UrlPatterns.MAIN) })
public class AuthorizationAccessFilter implements Filter {

    private String page;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        page = filterConfig.getInitParameter("page");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpSession session = hRequest.getSession();
        UserDto user = (UserDto) session.getAttribute("user");
        if (user != null) {
            HttpServletResponse hResponse = (HttpServletResponse) response;
            hResponse.sendRedirect(hRequest.getContextPath() + page);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}