package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.service.BicycleService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Command of showing page of creating new order
 * @author Elmax19
 * @version 1.0
 */
public enum ShowOrderCreationPage implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * {@link BicycleService} class to call
     */
    private final BicycleService bicycleService;
    /**
     * variables of parameters names
     */
    private static final String PLACE_PARAMETER_NAME = "place";
    private static final String DATE_PARAMETER_NAME = "date";
    /**
     * variable of bicycles attribute name
     */
    private static final String BICYCLES_ATTRIBUTE_NAME = "bicycles";

    /**
     * default class constructor - declare {@link ShowOrderCreationPage#bicycleService}
     */
    ShowOrderCreationPage() {
        this.bicycleService = new BicycleService();
    }

    /**
     * custom response to forward to new order page
     * @see ResponseContext
     */
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

    /**
     * execute method implementation {@link Command}:
     * give back possible bicycles to rent by cooke values: {@link ShowOrderCreationPage#DATE_PARAMETER_NAME} and
     * {@link ShowOrderCreationPage#PLACE_PARAMETER_NAME}
     * @param request received request
     * @return {@link ShowOrderCreationPage#CREATE_ORDER_RESPONSE}
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        String place = request.getCookieValue(PLACE_PARAMETER_NAME);
        if(request.getParameter(DATE_PARAMETER_NAME).equals("")){
            return ShowMainPage.INSTANCE.execute(request);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate date = LocalDate.parse(String.valueOf(request.getParameter(DATE_PARAMETER_NAME)), formatter);
        request.setAttribute(BICYCLES_ATTRIBUTE_NAME, bicycleService.findBicyclesToOder(place, date));
        return CREATE_ORDER_RESPONSE;
    }
}
