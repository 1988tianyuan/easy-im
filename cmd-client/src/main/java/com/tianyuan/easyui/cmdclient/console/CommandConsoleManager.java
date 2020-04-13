package com.tianyuan.easyui.cmdclient.console;

import java.util.Map;
import java.util.Scanner;

import com.google.common.collect.ImmutableMap;
import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.login.LoginConsole;

public class CommandConsoleManager {

    private final Map<ConsoleCommand, CmdConsole> consoleMap;
    
    public CommandConsoleManager(ChatContext chatContext) {
        consoleMap = initCmdConsoles(chatContext);
    }
    
    public void cmdLoop() {
        Scanner scanner = new Scanner(System.in);
        while (!Thread.interrupted()) {
            System.out.println("Please enter a command to begin a new operation: ");
            String cmd = scanner.next();
            exec(cmd, scanner);
        }
    }
    
    private void exec(String cmd, Scanner scanner) {
        ConsoleCommand command = ConsoleCommand.getCommand(cmd);
        if (!checkValidCmd(command)) {
            return;
        }
        if (ConsoleCommand.QUIT.equals(command)) {
            Thread.currentThread().interrupt();
            return;
        }
        CmdConsole cmdConsole = consoleMap.get(command);
        if (cmdConsole != null) {
            // begin a new loop in a specific console
            cmdConsole.exec(scanner);
        }
    }
    
    private Map<ConsoleCommand, CmdConsole> initCmdConsoles(ChatContext chatContext) {
        ImmutableMap.Builder<ConsoleCommand, CmdConsole> builder = ImmutableMap.builder();
        builder.put(ConsoleCommand.LOGIN, new LoginConsole(chatContext));
        return builder.build();
    }

    private boolean checkValidCmd(ConsoleCommand command) {
        if (command == null) {
            System.out.println("Your input command is invalid, " +
                    "please check, just these commands are valid: " + ConsoleCommand.allValuesStr() + "\n");
            return false;
        }
        return true;
    }
}
