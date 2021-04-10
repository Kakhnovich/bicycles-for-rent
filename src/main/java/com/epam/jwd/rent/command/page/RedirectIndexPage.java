package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;

/**
 * Command of redirecting to index page
 * @author Elmax19
 * @version 1.0
 */
public enum RedirectIndexPage implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * custom response to redirect to main page
     * @see ResponseContext
     */
    private static final ResponseContext INDEX_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/index.jsp";
        }

        @Override
        public boolean isRedirect() {
            return true;
        }
    };

    /**
     * execute method implementation {@link Command}:
     * redirect to index page
     * @param request received request
     * @return {@link RedirectIndexPage#INDEX_PAGE_RESPONSE}
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        return INDEX_PAGE_RESPONSE;
    }
}
