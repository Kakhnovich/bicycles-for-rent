package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.RedirectIndexPage;
import com.epam.jwd.rent.command.page.ShowAllUsersCommand;
import com.epam.jwd.rent.command.page.ShowMainPage;
import com.epam.jwd.rent.command.page.ShowLoginPage;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.service.UserService;
import com.epam.jwd.rent.session.ActiveUserSessions;

import java.util.Optional;

/**
 * Login user command
 * @author Elmax19
 * @version 1.0
 */
public enum LoginCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * {@link UserService} class to call
     */
    private final UserService userService;
    /**
     * class of active users sessions
     */
    private final ActiveUserSessions sessions = ActiveUserSessions.getInstance();
    /**
     * variables of parameters and attribute names
     */
    private static final String USER_NAME_PARAMETER_NAME = "userName";
    private static final String USER_PASSWORD_PARAMETER_NAME = "userPassword";
    private static final String USER_ATTRIBUTE_NAME = "user";
    /**
     * variables of error attributes name and value
     */
    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    private static final String ERROR_MESSAGE_ATTRIBUTE_VALUE = "invalid_credentials";

    /**
     * default class constructor - declare {@link LoginCommand#userService}
     */
    LoginCommand() {
        userService = new UserService();
    }

    /**
     * execute method implementation {@link Command}:
     * check form parameters and redirect to main page if all is OK, and to login page if credentials are invalid
     * @param request received request
     * @return {@link ShowLoginPage} or {@link RedirectIndexPage} response
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        final String name = String.valueOf(request.getParameter(USER_NAME_PARAMETER_NAME));
        final String password = String.valueOf(request.getParameter(USER_PASSWORD_PARAMETER_NAME));
        if(!name.equals("null") && !password.equals("null") &&
                !name.equals("") && !password.equals("")) {
            final Optional<UserDto> user = userService.login(name, password);
            ResponseContext result;
            if (user.isPresent()) {
                request.setSessionAttribute(USER_ATTRIBUTE_NAME, user.get());
                if (user.get().getRole().equals(USER_ATTRIBUTE_NAME)) {
                    sessions.addSession(user.get().getLogin(), request.getSession());
                }
                result = RedirectIndexPage.INSTANCE.execute(request);
            } else {
                request.setAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, ERROR_MESSAGE_ATTRIBUTE_VALUE);
                result = ShowLoginPage.INSTANCE.execute(request);
            }
            return result;
        }
        return ShowLoginPage.INSTANCE.execute(request);
    }
}
