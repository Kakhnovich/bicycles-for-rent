package com.epam.jwd.rent.command;

import javax.servlet.http.HttpSession;

public interface RequestContext {

    void setAttribute(String name, Object obj);

    Object getAttribute(String name);

    void removeSessionAttribute(String name);

    HttpSession getSession();

    Object getParameter(String name);

    void invalidateSession();

    void setSessionAttribute(String name, Object value);

    Object getSessionAttribute(String name);

}
