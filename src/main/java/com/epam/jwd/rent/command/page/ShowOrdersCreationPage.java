package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;

public enum ShowOrdersCreationPage implements Command {
    INSTANCE;

    private static final ResponseContext CREATE_ORDER_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/newOrder.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        return CREATE_ORDER_RESPONSE;
    }
}
