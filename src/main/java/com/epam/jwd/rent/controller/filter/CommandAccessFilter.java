package com.epam.jwd.rent.controller.filter;

import com.epam.jwd.rent.command.Command;
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

@WebFilter(urlPatterns = {"/controller"})
public class CommandAccessFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hRequest = (HttpServletRequest) request;
        HttpSession session = hRequest.getSession();
        UserDto user = (UserDto) session.getAttribute("user");
        int i = 0;
        for (CommandManager command : CommandManager.values()) {
            if (command.name().equalsIgnoreCase(String.valueOf(hRequest.getParameter("command")))) {
                break;
            }
            if (command.name().equalsIgnoreCase("DEFAULT")) {
                chain.doFilter(request, response);
            }
            i++;
        }
        if (user == null) {
            if (i > 3) {
                HttpServletResponse hResponse = (HttpServletResponse) response;
                hResponse.sendRedirect(hRequest.getContextPath() + UrlPatterns.LOGIN);
            } else{
                chain.doFilter(request, response);
            }
        } else if (i < 2) {
            HttpServletResponse hResponse = (HttpServletResponse) response;
            hResponse.sendRedirect(hRequest.getContextPath() + UrlPatterns.INDEX);
        } else if (user.getRole().equals("user") && i > 8){
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
