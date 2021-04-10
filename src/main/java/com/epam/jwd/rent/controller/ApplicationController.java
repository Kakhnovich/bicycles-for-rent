package com.epam.jwd.rent.controller;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.WrappingRequestContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet to process commands
 * @see HttpServlet
 * @author Elmax19
 * @version 1.0
 */
@WebServlet("/controller")
public class ApplicationController extends HttpServlet {

    /**
     * variables of parameters names
     */
    private static final String COMMAND_PARAMETER_NAME = "command";
    private static final String PLACE_PARAMETER_NAME = "place";
    private static final String DATE_PARAMETER_NAME = "date";
    /**
     * default command name
     */
    private static final String DEFAULT_COMMAND_NAME = "DEFAULT";
    /**
     * variables of commands values to compare with
     */
    private static final String ORDER_PAGE_COMMAND_VALUE = "order_page";
    private static final String CHANGE_LOCALE_COMMAND_VALUE = "change_locale";
    /**
     * variable of error attribute name
     */
    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    /**
     * main method to process doGet and doPost methods
     * @param req received request
     * @param resp received response
     */
    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter(COMMAND_PARAMETER_NAME);
        if (commandName == null) {
            commandName = DEFAULT_COMMAND_NAME;
        } else if(ORDER_PAGE_COMMAND_VALUE.equalsIgnoreCase(commandName)){
            String place = String.valueOf(req.getParameter(PLACE_PARAMETER_NAME));
            final String date = String.valueOf(req.getParameter(DATE_PARAMETER_NAME));
            resp.addCookie(new Cookie(PLACE_PARAMETER_NAME, place));
            resp.addCookie(new Cookie(DATE_PARAMETER_NAME, date));
        }
        final Command businessCommand = Command.of(commandName);
        final ResponseContext result = businessCommand.execute(WrappingRequestContext.of(req));
        req.setAttribute(COMMAND_PARAMETER_NAME, commandName);
        if(req.getAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME)==null){
            resp.addCookie(new Cookie(ERROR_MESSAGE_ATTRIBUTE_NAME,""));
        } else {
            resp.addCookie(new Cookie(ERROR_MESSAGE_ATTRIBUTE_NAME,
                    String.valueOf(req.getAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME))));
        }
        if (result.isRedirect() || CHANGE_LOCALE_COMMAND_VALUE.equalsIgnoreCase(commandName)){
            resp.sendRedirect(result.getPage());
        } else {
            final RequestDispatcher dispatcher = req.getRequestDispatcher(result.getPage());
            dispatcher.forward(req, resp);
        }
    }
}
