package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;

/**
 * Command of showing rating page
 * @author Elmax19
 * @version 1.0
 */
public enum ShowRatingPage implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * custom response to forward to rating page
     * @see ResponseContext
     */
    private static final ResponseContext RATING_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/rating.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    /**
     * execute method implementation {@link Command}:
     * forward to rating page
     * @param request received request
     * @return {@link ShowRatingPage#RATING_PAGE_RESPONSE}
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        return RATING_PAGE_RESPONSE;
    }
}
