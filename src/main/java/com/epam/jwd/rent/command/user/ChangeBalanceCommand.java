package com.epam.jwd.rent.command.user;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.command.page.ShowProfileCommand;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.UserDto;

import java.math.BigDecimal;

public enum ChangeBalanceCommand implements Command {
    INSTANCE;

    private final UserDao userDao;

    ChangeBalanceCommand() {
        userDao = new UserDao();
    }

    @Override
    public ResponseContext execute(RequestContext request) {
        BigDecimal money = BigDecimal.valueOf(Integer.parseInt(String.valueOf(request.getParameter("money"))));
        userDao.changeBalance(((UserDto) request.getSessionAttribute("user")).getLogin(), money);
        return ShowProfileCommand.INSTANCE.execute(request);
    }
}
