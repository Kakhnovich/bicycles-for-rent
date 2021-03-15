package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.dao.impl.OrderDao;
import com.epam.jwd.rent.model.Order;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum ShowAllOrdersCommand implements Command {
    INSTANCE;

    private final OrderDao orderDao;

    ShowAllOrdersCommand() {
        orderDao = new OrderDao();
    }

    private static final ResponseContext ALL_ORDERS_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/orders.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        Optional<List<Order>> allOrders = orderDao.findAll();
        if(allOrders.isPresent()) {
            if (String.valueOf(request.getParameter("byValue")).equals("date")) {
                allOrders.get().sort(Comparator.comparing(Order::getDate));
            } else if (String.valueOf(request.getParameter("byValue")).equals("user")) {
                allOrders.get().sort(Comparator.comparing(Order::getUser_id));
            } else if (String.valueOf(request.getParameter("byValue")).equals("status")) {
                allOrders.get().sort(Comparator.comparing(Order::getStatus));
            }
            request.setAttribute("orders", allOrders.get());
            List<Order> orders = allOrders.get().stream()
                    .filter(order -> order.getStatus().equals("in processing"))
                    .collect(Collectors.toList());
            request.setAttribute("ordersInProcess", orders);
        }
        setCurrentContextAttribute(request, ALL_ORDERS_PAGE_RESPONSE);
        return ALL_ORDERS_PAGE_RESPONSE;
    }
}

