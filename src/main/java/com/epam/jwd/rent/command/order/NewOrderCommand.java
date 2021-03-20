package com.epam.jwd.rent.command.order;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowMainPage;
import com.epam.jwd.rent.command.page.ShowOrderCreationPage;
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

    NewOrderCommand() {
        orderService = new OrderService();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        final String login = ((UserDto) request.getSessionAttribute("user")).getLogin();
        String place = request.getCookieValue("place");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate date = LocalDate.parse(request.getCookieValue("date"), formatter);
        String model = String.valueOf(request.getParameter("model"));
        String orderHours = String.valueOf(request.getParameter("hours"));
        final int hours = (orderHours.equals("null")) ? 1 : Integer.parseInt(orderHours);
        final Optional<OrderDto> order = orderService.createOrder(login, model, hours, date, place);
        if (order.isPresent()) {
            return ShowMainPage.INSTANCE.execute(request);
        } else {
            request.setAttribute("errorMessage", "Order processing error");
            return ShowOrderCreationPage.INSTANCE.execute(request);
        }
    }
}
