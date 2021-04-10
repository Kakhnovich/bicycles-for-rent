package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.dao.impl.BicycleDao;
import com.epam.jwd.rent.model.Place;

import java.util.List;
import java.util.Optional;

/**
 * Command of showing main page
 * @author Elmax19
 * @version 1.0
 */
public enum ShowMainPage implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * {@link BicycleDao} class to call
     */
    private final BicycleDao bicycleDao;

    /**
     * variable of places attribute name
     */
    private static final String PLACES_ATTRIBUTE_NAME = "places";

    /**
     * default class constructor - declare {@link ShowMainPage#bicycleDao}
     */
    ShowMainPage() {
        this.bicycleDao = new BicycleDao();
    }

    /**
     * custom response to forward to main page
     * @see ResponseContext
     */
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

    /**
     * execute method implementation {@link Command}:
     * set value of {@link ShowMainPage#PLACES_ATTRIBUTE_NAME}
     * @param request received request
     * @return {@link ShowMainPage#MAIN_PAGE_RESPONSE}
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        Optional<List<Place>> places = bicycleDao.findAllPlaces();
        places.ifPresent(address -> request.setAttribute(PLACES_ATTRIBUTE_NAME, address));
        return MAIN_PAGE_RESPONSE;
    }
}
