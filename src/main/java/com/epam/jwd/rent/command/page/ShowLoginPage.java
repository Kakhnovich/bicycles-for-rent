package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;

/**
 * Command of showing login page
 * @author Elmax19
 * @version 1.0
 */
public enum ShowLoginPage implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * custom response to redirect to login
     * @see ResponseContext
     */
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

    /**
     * execute method implementation {@link Command}:
     * redirect to login page
     * @param request received request
     * @return {@link ShowLoginPage#LOGIN_PAGE_RESPONSE} response
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        return LOGIN_PAGE_RESPONSE;
    }
}
