package com.epam.jwd.rent.controller;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.WrappingRequestContext;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/controller")
public class ApplicationController extends HttpServlet {

    private static final String COMMAND_PARAMETER_NAME = "command";
    private static final String DEFAULT_COMMAND_NAME = "DEFAULT";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        process(req, resp);
    }

    private void process(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String commandName = req.getParameter(COMMAND_PARAMETER_NAME);
        if (commandName == null) {
            commandName = DEFAULT_COMMAND_NAME;
        }
        final Command businessCommand = Command.of(commandName);
        final ResponseContext result = businessCommand.execute(WrappingRequestContext.of(req));
        if (result.isRedirect() || "CHANGE_LOCALE".equalsIgnoreCase(commandName)){
            resp.sendRedirect(result.getPage());
        } else {
            final RequestDispatcher dispatcher = req.getRequestDispatcher(result.getPage());
            dispatcher.forward(req, resp);
        }
    }
}
