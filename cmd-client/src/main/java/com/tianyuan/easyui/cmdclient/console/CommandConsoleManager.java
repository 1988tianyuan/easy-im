package com.tianyuan.easyui.cmdclient.console;

import com.google.common.collect.ImmutableMap;
import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.login.LoginConsole;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Scanner;

@Slf4j
public class CommandConsoleManager {

    private final Map<ConsoleCommand, CmdConsole> consoleMap;
    
    public CommandConsoleManager(ChatContext chatContext) {
        consoleMap = initCmdConsoles(chatContext);
    }
    
    public void cmdLoop() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        while (!exit) {
            System.out.println("Please enter a command to begin a new operation: ");
            ConsoleCommand command = ConsoleCommand.getCommand(scanner.next());
            if (!checkValidCmd(command)) {
                continue;
            }
            if (ConsoleCommand.QUIT.equals(command)) {
                System.out.println("Bye bye!");
                return;
            }
            try {
                exit = exec(command, scanner);
            } catch (Exception e) {
                log.error("Error happens when execute command:{}.", command, e);
            }
        }
    }
    
    private boolean exec(ConsoleCommand command, Scanner scanner) {
        CmdConsole cmdConsole = consoleMap.get(command);
        if (cmdConsole != null) {
            // begin a new loop in a specific console
            return cmdConsole.exec(scanner);
        }
        return false;
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
