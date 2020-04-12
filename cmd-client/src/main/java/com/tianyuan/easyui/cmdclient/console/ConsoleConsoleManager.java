package com.tianyuan.easyui.cmdclient.console;

import com.google.common.collect.ImmutableMap;
import com.tianyuan.easyui.cmdclient.exception.InvalidCommandException;

import java.util.Arrays;
import java.util.Map;

public class ConsoleConsoleManager {

    private static final Map<ConsoleCommand, CmdConsole> consoleMap;

    public void exec(String cmd) {
        ConsoleCommand command = ConsoleCommand.getCommand(cmd);
        checkValidCmd(command);
        CmdConsole cmdConsole = consoleMap.get(command);
        if (cmdConsole != null) {
            cmdConsole.exec(command);
        }
    }

    private void checkValidCmd(ConsoleCommand command) {
        if (command == null) {
            throw new InvalidCommandException("Your input command is invalid, " +
                    "please check, just there command is valid: " + Arrays.asList(ConsoleCommand.values()));
        }
    }

    static {
        ImmutableMap.Builder<ConsoleCommand, CmdConsole> builder = ImmutableMap.builder();
        builder.put(ConsoleCommand.LOGIN, new LoginConsole());
        consoleMap = builder.build();
    }
}
