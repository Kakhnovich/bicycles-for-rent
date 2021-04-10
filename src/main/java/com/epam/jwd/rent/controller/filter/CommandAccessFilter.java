package com.epam.jwd.rent.controller.filter;

import com.epam.jwd.rent.command.CommandManager;
import com.epam.jwd.rent.model.UserDto;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter possibilities about servlet commands
 * @see Filter
 * @author Elmax19
 * @version 1.0
 */
@WebFilter(urlPatterns = {"/controller"})
public class CommandAccessFilter implements Filter {
    /**
     * variables of attribute and parameter names
     */
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String COMMAND_PARAMETER_NAME = "command";
    /**
     * user role value
     */
    private static final String USER_ROLE_VALUE = "user";
    /**
     * default value of command
     */
    private static final String DEFAULT_COMMAND_VALUE = "DEFAULT";
    /**
     * command ids to compare
     */
    private static final int USER_COMMAND_MAX_ID = 8;
    private static final int VISITOR_COMMAND_MAX_ID = 3;
    private static final int USER_COMMAND_MIN_ID = 2;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpSession session = hRequest.getSession();
        UserDto user = (UserDto) session.getAttribute(USER_ATTRIBUTE_NAME);
        int i = 0;
        for (CommandManager command : CommandManager.values()) {
            if (command.name().equalsIgnoreCase(String.valueOf(hRequest.getParameter(COMMAND_PARAMETER_NAME)))) {
                break;
            }
            if (command.name().equalsIgnoreCase(DEFAULT_COMMAND_VALUE)) {
                chain.doFilter(request, response);
            }
            i++;
        }
        if (user == null) {
            if (i > VISITOR_COMMAND_MAX_ID && i<15) {
                HttpServletResponse hResponse = (HttpServletResponse) response;
                hResponse.sendRedirect(hRequest.getContextPath() + UrlPatterns.LOGIN);
            } else{
                chain.doFilter(request, response);
            }
        } else if (i < USER_COMMAND_MIN_ID) {
            HttpServletResponse hResponse = (HttpServletResponse) response;
            hResponse.sendRedirect(hRequest.getContextPath() + UrlPatterns.INDEX);
        } else if (user.getRole().equals(USER_ROLE_VALUE) && i > USER_COMMAND_MAX_ID){
            HttpServletResponse hResponse = (HttpServletResponse) response;
            hResponse.sendRedirect(hRequest.getContextPath() + UrlPatterns.INDEX);
        } else {
            chain.doFilter(request, response);
        }
    }

    @Override
    public void destroy() {

    }
}
