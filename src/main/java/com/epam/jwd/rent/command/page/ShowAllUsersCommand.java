package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public enum ShowAllUsersCommand implements Command {
    INSTANCE;

    private static final ResponseContext USERS_PAGE_RESPONSE = new ResponseContext() {
        @Override
        public String getPage() {
            return "/users.jsp";
        }

        @Override
        public boolean isRedirect() {
            return false;
        }
    };

    @Override
    public ResponseContext execute(RequestContext request) {
        Optional<List<User>> users = new UserDao().findAll();
        users.ifPresent(userList -> request.setAttribute("allUsers", userList));
        users.ifPresent(userList -> request.setAttribute("users", userList.stream()
                .filter(user -> user.getRoleId() == 2)
                .filter(user -> !user.isBanned())
                .collect(Collectors.toList())));
        return USERS_PAGE_RESPONSE;
    }
}
