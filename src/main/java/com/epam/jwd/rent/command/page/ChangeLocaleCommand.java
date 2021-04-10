package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.CommandManager;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;

/**
 * Command of changing locale
 * @author Elmax19
 * @version 1.0
 */
public enum ChangeLocaleCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * previous page value
     */
    private static String page;
    /**
     * variable of locale session attribute
     */
    private static final String LOCALE_ATTRIBUTE_NAME = "locale";

    /**
     * custom response to redirect to {@link ChangeLocaleCommand#page}
     * @see ResponseContext
     */
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

    /**
     * execute method implementation {@link Command}:
     * change site language
     * @param request received request
     * @return {@link ChangeLocaleCommand#LOCALE_RESPONSE}
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        request.removeSessionAttribute(LOCALE_ATTRIBUTE_NAME);
        request.setSessionAttribute(LOCALE_ATTRIBUTE_NAME, String.valueOf(request.getParameter(LOCALE_ATTRIBUTE_NAME)));
        page = request.getHeader("Referer");
        return LOCALE_RESPONSE;
    }
}
