package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowRatingPage;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.service.UserService;

import java.util.Map;

public enum RatingCommand implements Command {
    INSTANCE;

    private final UserDao userDao;

    RatingCommand() {
        userDao = new UserDao();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        request.setAttribute("rating", userDao.getRating("id"));
        request.setAttribute("hoursRating", userDao.getRating("hours"));
        return ShowRatingPage.INSTANCE.execute(request);
    }
}
