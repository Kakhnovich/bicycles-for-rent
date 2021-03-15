package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;

public enum ShowSignUpPage implements Command {
    INSTANCE;

    private static final ResponseContext SIGN_UP_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/signUp.jsp";
        }

        @Override
        public boolean isRedirect() {
            return true;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        setCurrentContextAttribute(request, SIGN_UP_PAGE_RESPONSE);
        return SIGN_UP_PAGE_RESPONSE;
    }
}
