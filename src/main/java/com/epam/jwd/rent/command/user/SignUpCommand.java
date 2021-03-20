package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowMainPage;
import com.epam.jwd.rent.command.page.ShowSignUpPage;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.service.UserService;
import com.epam.jwd.rent.session.ActiveUserSessions;

import java.util.Optional;

public enum SignUpCommand implements Command {
    INSTANCE;

    private final UserService userService;
    private final ActiveUserSessions sessions = ActiveUserSessions.getInstance();

    SignUpCommand() {
        userService = new UserService();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        final String name = String.valueOf(request.getParameter("userName"));
        final String password = String.valueOf(request.getParameter("userPassword"));
        final String repeatPassword = String.valueOf(request.getParameter("repeatPassword"));
        ResponseContext result;
        if (!password.equals(repeatPassword)){
            request.setAttribute("errorMessage", "invalid credentials");
            result = ShowSignUpPage.INSTANCE.execute(request);
        } else {
            final Optional<UserDto> user = userService.signUp(name, password);
            if (user.isPresent()) {
                request.setSessionAttribute("user", user.get());
                sessions.addSession(user.get().getLogin(), request.getSession());
                result = ShowMainPage.INSTANCE.execute(request);
            } else {
                request.setAttribute("errorMessage", "invalid credentials");
                result = ShowSignUpPage.INSTANCE.execute(request);
            }
        }
        return result;
    }
}
