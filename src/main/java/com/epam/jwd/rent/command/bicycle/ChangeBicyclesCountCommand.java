package com.epam.jwd.rent.command.bicycle;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowAllBicyclesCommand;
import com.epam.jwd.rent.service.BicycleService;

import java.math.BigDecimal;

public enum ChangeBicyclesCountCommand implements Command {
    INSTANCE;

    private final BicycleService bicycleService;

    ChangeBicyclesCountCommand() {
        bicycleService = new BicycleService();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        String model = String.valueOf(request.getParameter("model"));
        String place = String.valueOf(request.getParameter("place"));
        String cost = String.valueOf(request.getParameter("price"));
        BigDecimal price = BigDecimal.ZERO;
        if (!cost.equals("null")) {
            price = BigDecimal.valueOf(Double.parseDouble(cost));
        }
        price = price.setScale(2);
        int count = Integer.parseInt(String.valueOf(request.getParameter("count")));
        if (String.valueOf(request.getParameter("selectedOption")).equals("remove")) {
            count *= -1;
        }
        if (!model.equals("null") && !place.equals("null") && count != 0) {
            bicycleService.changeBicyclesCount(model, place, price, count);
        }
        return ShowAllBicyclesCommand.INSTANCE.execute(request);
    }
}
