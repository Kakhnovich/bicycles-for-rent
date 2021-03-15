package com.epam.jwd.rent.session;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

public class ActiveUserSessions {
    private final HashMap<String, HttpSession> activeSessions = new HashMap<>();
    private static final ActiveUserSessions INSTANCE = new ActiveUserSessions();

    private ActiveUserSessions() {
    }

    public static ActiveUserSessions getInstance() {
        return INSTANCE;
    }

    public void addSession(String userName, HttpSession session) {
        HttpSession previousSession = getSession(userName);
        if (previousSession != null) {
            removeSession(userName);
            previousSession.invalidate();
        }
        activeSessions.put(userName, session);
    }

    public void removeSession(String userName) {
        activeSessions.remove(userName);
    }

    public HttpSession getSession(String userName) {
        return activeSessions.get(userName);
    }
}
