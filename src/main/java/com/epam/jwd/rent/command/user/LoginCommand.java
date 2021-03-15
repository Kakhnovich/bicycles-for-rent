package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowMainPage;
import com.epam.jwd.rent.command.page.ShowLoginPage;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.service.UserService;
import com.epam.jwd.rent.session.ActiveUserSessions;

import java.util.Optional;

public enum LoginCommand implements Command {
    INSTANCE;

    private final UserService userService;
    private final ActiveUserSessions sessions = ActiveUserSessions.getInstance();

    LoginCommand() {
        userService = new UserService();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        final String name = String.valueOf(request.getParameter("userName"));
        final String password = String.valueOf(request.getParameter("userPassword"));
        final Optional<UserDto> user = userService.login(name, password);
        ResponseContext result;
        if (user.isPresent()) {
            request.setSessionAttribute("user", user.get());
            if (user.get().getRole().equals("user")) {
                sessions.addSession(user.get().getLogin(), request.getSession());
            }
            result = ShowMainPage.INSTANCE.execute(request);
        } else {
            request.setAttribute("errorMessage", "invalid credentials");
            result = ShowLoginPage.INSTANCE.execute(request);
        }
        return result;
    }
}
