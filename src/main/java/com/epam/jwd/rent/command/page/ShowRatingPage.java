package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;

public enum ShowRatingPage implements Command {
    INSTANCE;

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

    @Override
    public ResponseContext execute(RequestContext request) {
        return RATING_PAGE_RESPONSE;
    }
}
