package com.epam.jwd.rent.command.bicycle;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.RedirectIndexPage;
import com.epam.jwd.rent.command.page.ShowAllBicyclesCommand;
import com.epam.jwd.rent.service.BicycleService;

import java.math.BigDecimal;

/**
 * Command of changing count of bicycles
 * @author Elmax19
 * @version 1.0
 */
public enum ChangeBicyclesCountCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * variables of parameters names and values
     */
    private final static String MODEL_PARAMETER_NAME = "model";
    private final static String PLACE_PARAMETER_NAME = "place";
    private final static String COUNT_OF_BICYCLES_PARAMETER_NAME = "count";
    private final static String OPTION_PARAMETER_VALUE = "remove";
    private final static String OPTION_PARAMETER_NAME = "selectedOption";
    /**
     * variables of error attributes name and value
     */
    private static final String ERROR_MESSAGE_ATTRIBUTE_NAME = "errorMessage";
    private static final String ERROR_MESSAGE_ATTRIBUTE_VALUE = "Something_went_wrong";
    /**
     * {@link BicycleService} class to call
     */
    private final BicycleService bicycleService;

    /**
     * default class constructor - declare {@link ChangeBicyclesCountCommand#bicycleService}
     */
    ChangeBicyclesCountCommand() {
        bicycleService = new BicycleService();
    }

    /**
     * execute method implementation {@link Command}:
     * adds or removes count of bicycles by parameters: {@link ChangeBicyclesCountCommand#MODEL_PARAMETER_NAME},
     * {@link ChangeBicyclesCountCommand#PLACE_PARAMETER_NAME}
     * and {@link ChangeBicyclesCountCommand#COUNT_OF_BICYCLES_PARAMETER_NAME}
     * @param request received request
     * @return {@link RedirectIndexPage} response
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        String model = String.valueOf(request.getParameter(MODEL_PARAMETER_NAME));
        String place = String.valueOf(request.getParameter(PLACE_PARAMETER_NAME));
        String bicyclesCount = String.valueOf(request.getParameter(COUNT_OF_BICYCLES_PARAMETER_NAME));
        if(!model.equals("null") && !place.equals("null") && !bicyclesCount.equals("null") &&
                !model.equals("") && !place.equals("") && !bicyclesCount.equals("")) {
            int count = Integer.parseInt(bicyclesCount);
            if (String.valueOf(request.getParameter(OPTION_PARAMETER_NAME)).equals(OPTION_PARAMETER_VALUE)) {
                count *= -1;
            }
            if (count != 0) {
                if (!bicycleService.changeBicyclesCount(model, place, count)) {
                    request.setAttribute(ERROR_MESSAGE_ATTRIBUTE_NAME, ERROR_MESSAGE_ATTRIBUTE_VALUE);
                    return ShowAllBicyclesCommand.INSTANCE.execute(request);
                }
            }
            return RedirectIndexPage.INSTANCE.execute(request);
        }
        return ShowAllBicyclesCommand.INSTANCE.execute(request);
    }
}
