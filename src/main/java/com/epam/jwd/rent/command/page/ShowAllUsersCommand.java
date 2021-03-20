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

    private final UserDao userDao;

    ShowAllUsersCommand() {
        this.userDao = new UserDao();
    }

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
        String page = String.valueOf(request.getParameter("page"));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute("page", pageNumber);
        request.setAttribute("count", userDao.getCountOfPages(3));
        String column = String.valueOf(request.getParameter("column"));
        if(column.equals("null")){
            column = "id";
        }
        request.setAttribute("column", column);
        Optional<List<User>> users = userDao.findByPageNumber(column, pageNumber);
        users.ifPresent(userList -> request.setAttribute("allUsers", userList));
        return USERS_PAGE_RESPONSE;
    }
}
