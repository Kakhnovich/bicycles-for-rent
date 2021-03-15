package com.epam.jwd.rent.command.order;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowMainPage;
import com.epam.jwd.rent.command.page.ShowOrdersCreationPage;
import com.epam.jwd.rent.model.OrderDto;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.service.BicycleService;
import com.epam.jwd.rent.service.OrderService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

public enum NewOrderCommand implements Command {
    INSTANCE;

    private final OrderService orderService;
    private final BicycleService bicycleService;

    NewOrderCommand() {
        orderService = new OrderService();
        bicycleService = new BicycleService();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        ResponseContext result = ShowOrdersCreationPage.INSTANCE.execute(request);
        Optional<List<String>> allModels = bicycleService.findModels();
        Optional<List<String>> allPlaces = orderService.findPlaces();
        if (allModels.isPresent() && allPlaces.isPresent()) {
            request.setAttribute("models", allModels.get());
            request.setAttribute("places", allPlaces.get());
            if (request.getParameter("hours") != null) {
                final String login = ((UserDto) request.getSessionAttribute("user")).getLogin();
                final String model = String.valueOf(request.getParameter("orderModel"));
                final String place = String.valueOf(request.getParameter("orderPlace"));
                String orderHours = String.valueOf(request.getParameter("hours"));
                final int hours = (orderHours.equals("null")) ? 1 : Integer.parseInt(orderHours);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                final LocalDate date = LocalDate.parse(String.valueOf(request.getParameter("date")), formatter);
                final Optional<OrderDto> order = orderService.createOrder(login, model, hours, date, place);
                if (order.isPresent()) {
                    result = ShowMainPage.INSTANCE.execute(request);
                } else {
                    request.setAttribute("errorMessage", "invalid credentials");
                }
            }
        }
        return result;
    }
}
