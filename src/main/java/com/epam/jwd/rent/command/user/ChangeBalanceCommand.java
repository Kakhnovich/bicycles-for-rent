package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.RedirectIndexPage;
import com.epam.jwd.rent.command.page.ShowProfileCommand;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.UserDto;

import java.math.BigDecimal;

/**
 * Command of changing user balance
 * @author  Elmax19
 * @version 1.0
 */
public enum ChangeBalanceCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * {@link UserDao} class to call
     */
    private final UserDao userDao;
    /**
     * variables of attribute and parameter names
     */
    private static final String USER_ATTRIBUTE_NAME = "user";
    private static final String MONEY_PARAMETER_NAME = "money";

    /**
     * default class constructor - declare {@link ChangeBalanceCommand#userDao}
     */
    ChangeBalanceCommand() {
        userDao = new UserDao();
    }

    /**
     * execute method implementation {@link Command}:
     * change user balance by {@link ChangeBalanceCommand#MONEY_PARAMETER_NAME}
     * @param request received request
     * @return {@link RedirectIndexPage} response
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        if(String.valueOf(request.getParameter(MONEY_PARAMETER_NAME)).length()<5) {
            BigDecimal money = BigDecimal.valueOf(Integer.parseInt(String.valueOf(request.getParameter(MONEY_PARAMETER_NAME))));
            userDao.changeBalance(((UserDto) request.getSessionAttribute(USER_ATTRIBUTE_NAME)).getLogin(), money);
        }
        return RedirectIndexPage.INSTANCE.execute(request);
    }
}
