package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowRatingPage;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.service.UserService;

/**
 * Users rating command
 * @author Elmax19
 * @version 1.0
 */
public enum RatingCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * {@link UserService} class to call
     */
    private final UserService userService;
    /**
     * variables of attributes names
     */
    private static final String RATING_ATTRIBUTE_NAME = "rating";
    private static final String HOURS_RATING_ATTRIBUTE_NAME = "hoursRating";
    /**
     * variables of criteria values
     */
    private static final String ID_CRITERIA_VALUE = "id";
    private static final String HOURS_CRITERIA_VALUE = "hours";

    /**
     * default class constructor - declare {@link RatingCommand#userService}
     */
    RatingCommand() {
        userService = new UserService();
    }

    /**
     * execute method implementation {@link Command}:
     * give back two 'top 3' ratings by count of orders and sum of hours of orders
     * @param request received request
     * @return {@link ShowRatingPage} response
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        request.setAttribute(RATING_ATTRIBUTE_NAME, userService.getRating(ID_CRITERIA_VALUE));
        request.setAttribute(HOURS_RATING_ATTRIBUTE_NAME, userService.getRating(HOURS_CRITERIA_VALUE));
        return ShowRatingPage.INSTANCE.execute(request);
    }
}
