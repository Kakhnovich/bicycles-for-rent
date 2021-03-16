package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.CommandManager;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;

public enum ChangeLocaleCommand implements Command {
    INSTANCE;

    private static String page;

    private static final ResponseContext LOCALE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return page;
        }

        @Override
        public boolean isRedirect() {
            return true;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        request.removeSessionAttribute("locale");
        request.setSessionAttribute("locale", String.valueOf(request.getParameter("locale")));
        page = request.getHeader("Referer");
        return LOCALE_RESPONSE;
    }
}
