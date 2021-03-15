package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.model.OrderDto;
import com.epam.jwd.rent.model.UserDto;
import com.epam.jwd.rent.service.OrderService;
import com.epam.jwd.rent.service.UserService;

import java.util.List;
import java.util.Optional;

public enum ShowProfileCommand implements Command {
    INSTANCE;

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

    private final UserService userService;
    private final OrderService orderService;

    ShowProfileCommand() {
        userService = new UserService();
        orderService = new OrderService();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        Optional<UserDto> user = userService.findByLogin(String.valueOf(request.getSessionAttribute("name")));
        user.ifPresent(userDto -> request.setAttribute("user", userDto));
        Optional<List<OrderDto>> orders = orderService.findAllOrdersByUserName(((UserDto) request.getSessionAttribute("user")).getLogin());
        orders.ifPresent(orderDto -> request.setAttribute("orders", orderDto));
        setCurrentContextAttribute(request, PROFILE_PAGE_RESPONSE);
        return PROFILE_PAGE_RESPONSE;
        //todo remove hard code attributes names in all classes
    }
}