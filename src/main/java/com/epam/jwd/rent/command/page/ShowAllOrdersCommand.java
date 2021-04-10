package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.dao.impl.OrderDao;
import com.epam.jwd.rent.model.OrderDto;
import com.epam.jwd.rent.service.OrderService;

import java.util.List;
import java.util.Optional;

/**
 * Command of showing all orders
 * @author Elmax19
 * @version 1.0
 */
public enum ShowAllOrdersCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * {@link OrderService} class to call
     */
    private final OrderService orderService;
    /**
     * {@link OrderDao} class to call
     */
    private final OrderDao orderDao;
    /**
     * variables of bicycles attributes and parameters names
     */
    private static final String PAGE_PARAMETER_NAME = "page";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "count";
    private static final String SORT_COLUMN_PARAMETER_NAME = "column";
    private static final String ORDERS_ATTRIBUTE_NAME = "orders";
    /**
     * variable of default value of column for sort
     */
    private static final String DEFAULT_SORT_COLUMN_VALUE = "id";

    /**
     * default class constructor - declare {@link ShowAllOrdersCommand#orderService} and
     * {@link ShowAllOrdersCommand#orderDao}
     */
    ShowAllOrdersCommand() {
        orderService = new OrderService();
        orderDao = new OrderDao();
    }

    /**
     * custom response to forward to orders page
     * @see ResponseContext
     */
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

    /**
     * execute method implementation {@link Command}:
     * give back all bicycles by parameters: {@link ShowAllOrdersCommand#SORT_COLUMN_PARAMETER_NAME} and
     * {@link ShowAllOrdersCommand#PAGE_PARAMETER_NAME}
     * @param request received request
     * @return {@link ShowAllOrdersCommand#ALL_ORDERS_PAGE_RESPONSE}
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        String page = String.valueOf(request.getParameter(PAGE_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_PARAMETER_NAME, pageNumber);
        request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, orderDao.getCountOfPages(7));
        String column = String.valueOf(request.getParameter(SORT_COLUMN_PARAMETER_NAME));
        if(column.equals("null")){
            column = DEFAULT_SORT_COLUMN_VALUE;
        }
        request.setAttribute(SORT_COLUMN_PARAMETER_NAME, column);
        Optional<List<OrderDto>> orders = orderService.findByPage(column, pageNumber);
        orders.ifPresent(orderList -> request.setAttribute(ORDERS_ATTRIBUTE_NAME, orderList));
        return ALL_ORDERS_PAGE_RESPONSE;
    }
}

