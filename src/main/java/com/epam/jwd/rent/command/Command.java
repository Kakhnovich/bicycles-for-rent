package com.epam.jwd.rent.command;

public interface Command {

    ResponseContext execute(RequestContext request);

    static Command of(String name) {
        return CommandManager.of(name);
    }

    default void setCurrentContextAttribute(RequestContext request, ResponseContext context){
        request.removeSessionAttribute("context");
        request.setSessionAttribute("context", context);
    }
}
