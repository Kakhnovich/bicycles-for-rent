package com.epam.jwd.rent.command;

public interface Command {

    ResponseContext execute(RequestContext request);

    static Command of(String name) {
        return CommandManager.of(name);
    }
}
