package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.service.BicycleService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public enum ShowOrderCreationPage implements Command {
    INSTANCE;

    private final BicycleService bicycleService;

    ShowOrderCreationPage() {
        this.bicycleService = new BicycleService();
    }

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
        String place = request.getCookieValue("place");
        if(request.getCookieValue("date").equals("null")){
            request.setAttribute("errorMessage", "please choose date");
            return ShowMainPage.INSTANCE.execute(request);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate date = LocalDate.parse(request.getCookieValue("date"), formatter);
        request.setAttribute("bicycles", bicycleService.findBicyclesToOder(place, date));
        return CREATE_ORDER_RESPONSE;
    }
}
