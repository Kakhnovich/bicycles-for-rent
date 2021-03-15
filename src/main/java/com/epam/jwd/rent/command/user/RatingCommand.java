package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowRatingPage;
import com.epam.jwd.rent.service.UserService;

import java.util.Map;

public enum RatingCommand implements Command {
    INSTANCE;

    private final UserService userService;

    RatingCommand() {
        userService = new UserService();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        final Map<String, Integer> rating = userService.rating();
        request.setAttribute("rating", rating);
        final Map<String, Integer> hoursRating = userService.hoursRating();
        request.setAttribute("hoursRating", hoursRating);
        return ShowRatingPage.INSTANCE.execute(request);
    }
}
