package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.dao.impl.OrderDao;
import com.epam.jwd.rent.model.OrderDto;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.service.OrderService;
import com.epam.jwd.rent.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Command of showing user profile page
 * @author Elmax19
 * @version 1.0
 */
public enum ShowProfileCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * custom response to forward to profile page
     * @see ResponseContext
     */
    private static final ResponseContext PROFILE_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/profile.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    /**
     * {@link UserService} class to call
     */
    private final UserService userService;
    /**
     * {@link OrderService} class to call
     */
    private final OrderService orderService;
    /**
     * variables of bicycles attributes and parameters names
     */
    private static final String PAGE_PARAMETER_NAME = "page";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "count";
    private static final String ORDERS_ATTRIBUTE_NAME = "orders";
    private static final String USER_ATTRIBUTE_NAME = "user";

    /**
     * default class constructor - declare {@link ShowProfileCommand#userService} and
     * {@link ShowProfileCommand#orderService}
     */
    ShowProfileCommand() {
        userService = new UserService();
        orderService = new OrderService();
    }

    /**
     * execute method implementation {@link Command}:
     * give back user info by user login
     * @param request received request
     * @return {@link ShowProfileCommand#PROFILE_PAGE_RESPONSE}
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        Optional<UserDto> user = userService.findByLogin(((UserDto)request.getSessionAttribute(USER_ATTRIBUTE_NAME)).getLogin());
        //todo check
        user.ifPresent(userDto -> request.setAttribute(USER_ATTRIBUTE_NAME, userDto));
        String page = String.valueOf(request.getParameter(PAGE_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_PARAMETER_NAME, pageNumber);
        Optional<List<OrderDto>> orders = orderService.findAllOrdersByUserName(((UserDto) request.getSessionAttribute(USER_ATTRIBUTE_NAME)).getLogin());
        if (orders.isPresent()) {
            request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, (orders.get().size()+4)/5);
            List<OrderDto> ordersPage = new ArrayList<>();
            for(int i=3*(pageNumber-1);i<3*pageNumber && i< orders.get().size();i++){
                ordersPage.add(orders.get().get(i));
            }
            request.setAttribute(ORDERS_ATTRIBUTE_NAME, ordersPage);
        }
        return PROFILE_PAGE_RESPONSE;
    }
}