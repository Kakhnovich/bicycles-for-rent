package com.epam.jwd.rent.command.order;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.RedirectIndexPage;
import com.epam.jwd.rent.command.page.ShowMainPage;
import com.epam.jwd.rent.model.OrderDto;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.service.OrderService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * Command of creation new Order
 */
public enum NewOrderCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * {@link OrderService} class to call
     */
    private final OrderService orderService;
    /**
     * variable of cookies names
     */
    private static final String COOKIE_PLACE_NAME = "place";
    private static final String COOKIE_DATE_NAME = "date";
    /**
     * variables of parameters and attributes names
     */
    private static final String USER_PARAMETER_NAME = "user";
    private static final String MODEL_PARAMETER_NAME = "model";
    private static final String HOURS_PARAMETER_NAME = "hours";
    /**
     * variables of error attributes name and value
     */
    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    private static final String ERROR_MESSAGE_ATTRIBUTE_VALUE = "Order_processing_error";


    /**
     * default class constructor - declare {@link NewOrderCommand#orderService}
     */
    NewOrderCommand() {
        orderService = new OrderService();
    }

    /**
     * execute method implementation {@link Command}:
     * creates new order by username, date, bicycle model, count of hours and place of rent
     *
     * @param request received request
     * @return {@link RedirectIndexPage} response
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        final String login = ((UserDto) request.getSessionAttribute(USER_PARAMETER_NAME)).getLogin();
        String place = request.getCookieValue(COOKIE_PLACE_NAME);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final LocalDate date = LocalDate.parse(request.getCookieValue(COOKIE_DATE_NAME), formatter);
        String model = String.valueOf(request.getParameter(MODEL_PARAMETER_NAME));
        String orderHours = String.valueOf(request.getParameter(HOURS_PARAMETER_NAME));
        final int hours = (orderHours.equals("null")) ? 1 : Integer.parseInt(orderHours);
        final Optional<OrderDto> order = orderService.createOrder(login, model, hours, date, place);
        if (order.isPresent()) {
            return ShowMainPage.INSTANCE.execute(request);
        } else {
            request.setAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, ERROR_MESSAGE_ATTRIBUTE_VALUE);
            return RedirectIndexPage.INSTANCE.execute(request);
        }
    }
}
