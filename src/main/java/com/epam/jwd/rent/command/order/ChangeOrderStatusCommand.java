package com.epam.jwd.rent.command.order;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowAllOrdersCommand;
import com.epam.jwd.rent.service.OrderService;

/**
 * Command of changing order status
 *
 * @author Elmax19
 * @version 1.0
 */
public enum ChangeOrderStatusCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * {@link OrderService} class to call
     */
    private final OrderService orderService;
    /**
     * possible values of status field
     */
    private static final String acceptedStatus = "accepted";
    private static final String canceledStatus = "canceled";
    /**
     * variables of parameters names and values
     */
    private static final String SELECTED_ORDER_PARAMETER_NAME = "selectedOrder";
    private static final String SELECTED_ORDER_PARAMETER_WRONG_VALUE = "Please Select";
    private static final String OPTION_PARAMETER_NAME = "option";
    private static final String OPTION_PARAMETER_VALUE = "accept";

    /**
     * default class constructor - declare {@link ChangeOrderStatusCommand#orderService}
     */
    ChangeOrderStatusCommand() {
        orderService = new OrderService();
    }

    /**
     * execute method implementation {@link Command}:
     * changes order status by parameters: {@link ChangeOrderStatusCommand#SELECTED_ORDER_PARAMETER_NAME} and
     * {@link ChangeOrderStatusCommand#OPTION_PARAMETER_NAME}
     * @param request received request
     * @return {@link ShowAllOrdersCommand} response
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        if (!String.valueOf(request.getParameter(SELECTED_ORDER_PARAMETER_NAME)).equals(SELECTED_ORDER_PARAMETER_WRONG_VALUE)) {
            String status = String.valueOf(request.getParameter(OPTION_PARAMETER_NAME)).equals(OPTION_PARAMETER_VALUE)
                    ? acceptedStatus : canceledStatus;
            int id = Integer.parseInt((String.valueOf(request.getParameter(SELECTED_ORDER_PARAMETER_NAME))));
            orderService.changeStatus(id, status);
        }
        return ShowAllOrdersCommand.INSTANCE.execute(request);
    }
}
