package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.dao.impl.BicycleDao;
import com.epam.jwd.rent.model.BicycleDto;
import com.epam.jwd.rent.service.BicycleService;
import com.epam.jwd.rent.service.CommonService;

import java.util.Collections;
import java.util.List;

public enum ShowAllBicyclesCommand implements Command {
    INSTANCE;

    private static final ResponseContext BICYCLES_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/bicycles.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    private static final String BICYCLES_ATTRIBUTE_NAME = "bicycles";

    private final BicycleService bicycleService;

    ShowAllBicyclesCommand() {
        bicycleService = new BicycleService();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        String page = String.valueOf(request.getParameter("page"));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute("page", pageNumber);
        request.setAttribute("count", new BicycleDao().getCountOfPages(3));
        String column = String.valueOf(request.getParameter("column"));
        if(column.equals("null")){
            column = "model";
        }
        request.setAttribute("column", column);
        final List<BicycleDto> bicycles = bicycleService.findByPage(column, pageNumber).orElse(Collections.emptyList());
        request.setAttribute(BICYCLES_ATTRIBUTE_NAME, bicycles);
        return BICYCLES_PAGE_RESPONSE;
    }
}
