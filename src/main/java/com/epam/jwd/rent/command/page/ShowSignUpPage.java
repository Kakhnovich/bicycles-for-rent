package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;

/**
 * Command of showing registration page
 * @author Elmax19
 * @version 1.0
 */
public enum ShowSignUpPage implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * custom response to redirect to registration page
     * @see ResponseContext
     */
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

    /**
     * execute method implementation {@link Command}:
     * redirect to registration page
     * @param request received request
     * @return {@link ShowSignUpPage#SIGN_UP_PAGE_RESPONSE}
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        return SIGN_UP_PAGE_RESPONSE;
    }
}
