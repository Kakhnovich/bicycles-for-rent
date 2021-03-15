package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;

public enum ShowLoginPage implements Command {
    INSTANCE;

    private static final ResponseContext LOGIN_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/login.jsp";
        }

        @Override
        public boolean isRedirect() {
            return true;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        setCurrentContextAttribute(request, LOGIN_PAGE_RESPONSE);
        return LOGIN_PAGE_RESPONSE;
    }
}
