package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowAllUsersCommand;
import com.epam.jwd.rent.command.page.ShowSignUpPage;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.service.UserService;
import com.epam.jwd.rent.session.ActiveUserSessions;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Command of changing user status or role
 * @author Elmax19
 * @version 1.0
 */
public enum ChangeUserInformationCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * {@link UserDao} class to call
     */
    private final UserDao userDao;
    /**
     * variables of parameters names
     */
    private static final String OPTION_PARAMETER_NAME = "option";
    private static final String SELECTED_USER_PARAMETER_NAME = "selectedUser";
    /**
     * value of 'ban' option
     */
    private static final String BAN_OPTION_VALUE = "ban";

    /**
     * default class constructor - declare {@link ChangeUserInformationCommand#userDao}
     */
    ChangeUserInformationCommand() {
        userDao = new UserDao();
    }

    /**
     * execute method implementation {@link Command}:
     * ban or promote user to Admin by {@link ChangeUserInformationCommand#OPTION_PARAMETER_NAME}
     * @param request received request
     * @return {@link ShowAllUsersCommand} response
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        String option = String.valueOf(request.getParameter(OPTION_PARAMETER_NAME));
        String login = String.valueOf(request.getParameter(SELECTED_USER_PARAMETER_NAME));
        ActiveUserSessions sessions = ActiveUserSessions.getInstance();
        if (!login.equals("null")) {
            Optional<UserDto> user = new UserService().findByLogin(login);
            HttpSession session = sessions.getSession(user.get().getLogin());
            if (option.equals(BAN_OPTION_VALUE)) {
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
