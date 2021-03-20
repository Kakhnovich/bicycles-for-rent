package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.dao.impl.BicycleDao;

import java.util.List;
import java.util.Optional;

public enum ShowMainPage implements Command {
    INSTANCE;

    private final BicycleDao bicycleDao;

    ShowMainPage() {
        this.bicycleDao = new BicycleDao();
    }

    private static final ResponseContext MAIN_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/main.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        Optional<List<String>> places = bicycleDao.findAllPlaces();
        places.ifPresent(address -> request.setAttribute("places", address));
        return MAIN_PAGE_RESPONSE;
    }
}
