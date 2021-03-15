package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowMainPage;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.session.ActiveUserSessions;

public enum LogoutCommand implements Command {
    INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        ActiveUserSessions.getInstance().removeSession(((UserDto) request.getSessionAttribute("user")).getLogin());
        request.invalidateSession();
        return ShowMainPage.INSTANCE.execute(request);
    }
}
