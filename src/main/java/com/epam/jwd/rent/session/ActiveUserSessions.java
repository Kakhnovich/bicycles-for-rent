package com.epam.jwd.rent.session;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

/**
 * Class for mapping active user sessions
 * @author Elmax19
 * @version 1.0
 */
public class ActiveUserSessions {
    /**
     * map of users active sessions to stop it at for ban in future
     */
    private final HashMap<String, HttpSession> activeSessions = new HashMap<>();
    /**
     * singleton realisation with lazy initialisation
     */
    private static final ActiveUserSessions INSTANCE = new ActiveUserSessions();

    /**
     * default class constructor
     */
    private ActiveUserSessions() {
    }

    /**
     * singleton getter
     * @return {@link ActiveUserSessions#INSTANCE}
     */
    public static ActiveUserSessions getInstance() {
        return INSTANCE;
    }

    /**
     * method to add new active session into map
     * @param userName name of new authorized user
     * @param session his session
     */
    public void addSession(String userName, HttpSession session) {
        HttpSession previousSession = getSession(userName);
        if (previousSession != null) {
            removeSession(userName);
            previousSession.invalidate();
        }
        activeSessions.put(userName, session);
    }

    /**
     * method to remove active session from map
     * @param userName name of banned user
     */
    public void removeSession(String userName) {
        activeSessions.remove(userName);
    }

    /**
     * method to give back one of sessions
     * @param userName banned user name
     * @return his session to invalidate
     */
    public HttpSession getSession(String userName) {
        return activeSessions.get(userName);
    }
}
