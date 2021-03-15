package com.epam.jwd.rent.command.locale;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowMainPage;

public enum ChangeLocaleCommand implements Command {
    INSTANCE;

    @Override
    public ResponseContext execute(RequestContext request) {
        request.removeSessionAttribute("locale");
        request.setSessionAttribute("locale", String.valueOf(request.getParameter("locale")));
        ResponseContext responseContext = (ResponseContext) request.getSessionAttribute("context");
        if (responseContext==null){
            return ShowMainPage.INSTANCE.execute(request);
        } else return responseContext;
    }
}
