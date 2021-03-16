package com.epam.jwd.rent.command;

import com.epam.jwd.rent.command.bicycle.ChangeBicyclesCountCommand;
import com.epam.jwd.rent.command.page.ChangeLocaleCommand;
import com.epam.jwd.rent.command.order.ChangeOrderStatusCommand;
import com.epam.jwd.rent.command.order.NewOrderCommand;
import com.epam.jwd.rent.command.page.ShowAllBicyclesCommand;
import com.epam.jwd.rent.command.page.ShowAllOrdersCommand;
import com.epam.jwd.rent.command.page.ShowMainPage;
import com.epam.jwd.rent.command.page.ShowProfileCommand;
import com.epam.jwd.rent.command.page.ShowAllUsersCommand;
import com.epam.jwd.rent.command.user.ChangeBalanceCommand;
import com.epam.jwd.rent.command.user.ChangeUserInformationCommand;
import com.epam.jwd.rent.command.user.LoginCommand;
import com.epam.jwd.rent.command.user.LogoutCommand;
import com.epam.jwd.rent.command.user.RatingCommand;
import com.epam.jwd.rent.command.user.SignUpCommand;

public enum CommandManager {
    DEFAULT(ShowMainPage.INSTANCE),
    LOGIN(LoginCommand.INSTANCE),
    SIGN_UP(SignUpCommand.INSTANCE),
    CHANGE_LOCALE(ChangeLocaleCommand.INSTANCE),
    LOGOUT(LogoutCommand.INSTANCE),
    RATING(RatingCommand.INSTANCE),
    SHOW_BICYCLES(ShowAllBicyclesCommand.INSTANCE),
    NEW_ORDER(NewOrderCommand.INSTANCE),
    PROFILE(ShowProfileCommand.INSTANCE),
    CHANGE_BALANCE(ChangeBalanceCommand.INSTANCE),
    CHANGE_BICYCLES_COUNT(ChangeBicyclesCountCommand.INSTANCE),
    ORDERS(ShowAllOrdersCommand.INSTANCE),
    CHANGE_ORDER_STATUS(ChangeOrderStatusCommand.INSTANCE),
    USERS(ShowAllUsersCommand.INSTANCE),
    CHANGE_USER_INFORMATION(ChangeUserInformationCommand.INSTANCE);

    private final Command command;

    CommandManager(Command command) {
        this.command = command;
    }

    static Command of(String name) {
        for (CommandManager action : values()) {
            if (action.name().equalsIgnoreCase(name)) {
                return action.command;
            }
        }
        return DEFAULT.command;
    }
}
