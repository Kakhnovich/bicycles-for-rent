package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.RedirectIndexPage;
import com.epam.jwd.rent.command.page.ShowMainPage;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.session.ActiveUserSessions;

/**
 * Logout command
 *
 * @author Elmax19
 * @version 1.0
 */
public enum LogoutCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * variable of user session attribute
     */
    private static final String USER_ATTRIBUTE_NAME = "user";

    /**
     * execute method implementation {@link Command}:
     * remove session by {@link LogoutCommand#USER_ATTRIBUTE_NAME}
     *
     * @param request received request
     * @return {@link RedirectIndexPage} response
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        ActiveUserSessions.getInstance().removeSession(((UserDto) request.getSessionAttribute(USER_ATTRIBUTE_NAME)).getLogin());
        request.invalidateSession();
        return RedirectIndexPage.INSTANCE.execute(request);
    }
}
