package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
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

    private final CommonService<BicycleDto> bicycleService;

    ShowAllBicyclesCommand() {
        bicycleService = new BicycleService();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        final List<BicycleDto> bicycles = bicycleService.findAll().orElse(Collections.emptyList());
        request.setAttribute(BICYCLES_ATTRIBUTE_NAME, bicycles);
        return BICYCLES_PAGE_RESPONSE;
    }
}
