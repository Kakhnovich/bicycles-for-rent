package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowAllUsersCommand;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.service.UserService;
import com.epam.jwd.rent.session.ActiveUserSessions;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public enum ChangeUserInformationCommand implements Command {
    INSTANCE;

    private final UserDao userDao;

    ChangeUserInformationCommand() {
        userDao = new UserDao();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        String option = String.valueOf(request.getParameter("option"));
        String login = String.valueOf(request.getParameter("selectedUser"));
        ActiveUserSessions sessions = ActiveUserSessions.getInstance();
        if (!login.equals("null")) {
            Optional<UserDto> user = new UserService().findByLogin(login);
            HttpSession session = sessions.getSession(user.get().getLogin());
            if (option.equals("ban")) {
                if (session!=null) {
                    sessions.removeSession(user.get().getLogin());
                    session.invalidate();
                }
                userDao.banUser(login);
            } else {
                userDao.promoteUser(login);
            }
        }
        return ShowAllUsersCommand.INSTANCE.execute(request);
    }
}
