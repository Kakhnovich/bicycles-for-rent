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

/**
 * Filter for User possibilities about jsp pages
 * @see Filter
 * @author Elmax19
 * @version 1.0
 */
@WebFilter(urlPatterns = {UrlPatterns.NEW_ORDER}, initParams = {
        @WebInitParam(name = "page", value = UrlPatterns.INDEX) })
public class UserAccessFilter implements Filter {
    /**
     * variable of page to redirect to
     */
    private String page;
    /**
     * variable of attribute and parameter names
     */
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String INIT_PARAMETER_NAME = "page";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        page = filterConfig.getInitParameter(INIT_PARAMETER_NAME);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpSession session = hRequest.getSession();
        UserDto user = (UserDto) session.getAttribute(USER_ATTRIBUTE_NAME);
        if(user == null || !user.getRole().equals(USER_ATTRIBUTE_NAME)) {
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
