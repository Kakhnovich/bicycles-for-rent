package com.epam.jwd.rent.command.order;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowAllOrdersCommand;
import com.epam.jwd.rent.service.OrderService;

public enum ChangeOrderStatusCommand implements Command {
    INSTANCE;

    private final OrderService orderService;
    private static final String acceptedStatus = "accepted";
    private static final String canceledStatus = "canceled";

    ChangeOrderStatusCommand() {
        orderService = new OrderService();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        if (!String.valueOf(request.getParameter("selectedOrder")).equals("Please Select")) {
            String status = (request.getParameter("chkBox")) != null ? acceptedStatus : canceledStatus;
            int id = Integer.parseInt((String.valueOf(request.getParameter("selectedOrder"))));
            orderService.changeStatus(id, status);
        }
        return ShowAllOrdersCommand.INSTANCE.execute(request);
    }
}
