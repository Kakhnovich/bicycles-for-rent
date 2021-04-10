package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.dao.impl.BicycleDao;
import com.epam.jwd.rent.model.BicycleDto;
import com.epam.jwd.rent.service.BicycleService;

import java.util.Collections;
import java.util.List;

/**
 * Command of showing info about all bicycles
 *
 * @author Elmax19
 * @version 1.0
 */
public enum ShowAllBicyclesCommand implements Command {
    /**
     * Singleton realization
     */
    INSTANCE;

    /**
     * custom response to forward to bicycles page
     *
     * @see ResponseContext
     */
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

    /**
     * variables of bicycles attributes and parameters names
     */
    private static final String BICYCLES_ATTRIBUTE_NAME = "bicycles";
    private static final String PAGE_PARAMETER_NAME = "page";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "count";
    private static final String SORT_COLUMN_PARAMETER_NAME = "column";
    /**
     * variable of default value of column for sort
     */
    private static final String DEFAULT_SORT_COLUMN_VALUE = "model";

    /**
     * {@link BicycleService} class to call
     */
    private final BicycleService bicycleService;

    /**
     * default class constructor - declare {@link ShowAllBicyclesCommand#bicycleService}
     */
    ShowAllBicyclesCommand() {
        bicycleService = new BicycleService();
    }

    /**
     * execute method implementation {@link Command}:
     * give back all bicycles by parameters: {@link ShowAllBicyclesCommand#PAGE_PARAMETER_NAME} and
     * {@link ShowAllBicyclesCommand#SORT_COLUMN_PARAMETER_NAME}
     *
     * @param request received request
     * @return {@link ShowAllBicyclesCommand#BICYCLES_PAGE_RESPONSE}
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        String page = String.valueOf(request.getParameter(PAGE_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_PARAMETER_NAME, pageNumber);
        request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, new BicycleDao().getCountOfPages(5));
        String column = String.valueOf(request.getParameter(SORT_COLUMN_PARAMETER_NAME));
        if (column.equals("null")) {
            column = DEFAULT_SORT_COLUMN_VALUE;
        }
        request.setAttribute(SORT_COLUMN_PARAMETER_NAME, column);
        final List<BicycleDto> bicycles = bicycleService.findByPage(column, pageNumber).orElse(Collections.emptyList());
        request.setAttribute(BICYCLES_ATTRIBUTE_NAME, bicycles);
        return BICYCLES_PAGE_RESPONSE;
    }
}
