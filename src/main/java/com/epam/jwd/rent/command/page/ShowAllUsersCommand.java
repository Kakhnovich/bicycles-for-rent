package com.epam.jwd.rent.command.page;

import com.epam.jwd.rent.command.Command;
import com.epam.jwd.rent.command.RequestContext;
import com.epam.jwd.rent.command.ResponseContext;
import com.epam.jwd.rent.dao.impl.UserDao;
import com.epam.jwd.rent.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Command of showing all users
 * @author Elmax19
 * @version 1.0
 */
public enum ShowAllUsersCommand implements Command {
    /**
     * Singleton realisation
     */
    INSTANCE;

    /**
     * {@link UserDao} class to call
     */
    private final UserDao userDao;
    /**
     * variables of bicycles attributes and parameters names
     */
    private static final String PAGE_PARAMETER_NAME = "page";
    private static final String COUNT_OF_PAGES_ATTRIBUTE_NAME = "count";
    private static final String SORT_COLUMN_PARAMETER_NAME = "column";
    private static final String USERS_ATTRIBUTE_NAME = "allUsers";
    /**
     * variable of default value of column for sort
     */
    private static final String DEFAULT_SORT_COLUMN_VALUE = "id";

    /**
     * default class constructor - declare {@link ShowAllUsersCommand#userDao}
     */
    ShowAllUsersCommand() {
        this.userDao = new UserDao();
    }

    /**
     * custom response to forward to users page
     * @see ResponseContext
     */
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

    /**
     * execute method implementation {@link Command}:
     * give back all bicycles by parameters: {@link ShowAllUsersCommand#SORT_COLUMN_PARAMETER_NAME} and
     * {@link ShowAllUsersCommand#PAGE_PARAMETER_NAME}
     * @param request received request
     * @return {@link ShowAllUsersCommand#USERS_PAGE_RESPONSE}
     */
    @Override
    public ResponseContext execute(RequestContext request) {
        String page = String.valueOf(request.getParameter(PAGE_PARAMETER_NAME));
        final int pageNumber = (page.equals("null")) ? 1 : Integer.parseInt(page);
        request.setAttribute(PAGE_PARAMETER_NAME, pageNumber);
        request.setAttribute(COUNT_OF_PAGES_ATTRIBUTE_NAME, userDao.getCountOfPages(5));
        String column = String.valueOf(request.getParameter(SORT_COLUMN_PARAMETER_NAME));
        if(column.equals("null")){
            column = DEFAULT_SORT_COLUMN_VALUE;
        }
        request.setAttribute(SORT_COLUMN_PARAMETER_NAME, column);
        Optional<List<User>> users = userDao.findByPageNumber(column, pageNumber);
        users.ifPresent(userList -> request.setAttribute(USERS_ATTRIBUTE_NAME, userList));
        return USERS_PAGE_RESPONSE;
    }
}
