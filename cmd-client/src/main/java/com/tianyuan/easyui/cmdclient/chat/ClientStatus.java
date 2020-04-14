package com.tianyuan.easyui.cmdclient.chat;

import com.tianyuan.easyui.cmdclient.console.ConsoleCommand;

import java.util.EnumMap;

import static com.tianyuan.easyui.cmdclient.console.ConsoleCommand.CHAT_WITH_USER;
import static com.tianyuan.easyui.cmdclient.console.ConsoleCommand.LOGIN;

public enum ClientStatus {
    INIT, LOGGED, QUIT, CHATTING;

    private EnumMap<ConsoleCommand, String> invalidCommandMap = new EnumMap<>(ConsoleCommand.class);

    public boolean isValid(ConsoleCommand command) {
        if (command == null) {
            System.out.println("Your input command is invalid, " +
                    "please check, just these commands are valid: " + ConsoleCommand.allValuesStr() + "\n");
            return false;
        }
        if (invalidCommandMap.containsKey(command)) {
            System.out.println(invalidCommandMap.get(command));
            return false;
        }
        return true;
    }

    static  {
        LOGGED.addInvalidCommand(LOGIN, "You have already logged, please logout firstly.");
        CHATTING.addInvalidCommand(LOGIN, "You have already logged, please logout firstly.");
        INIT.addInvalidCommand(CHAT_WITH_USER, "You should login firstly before chat.");
        INIT.addInvalidCommand(ConsoleCommand.LOGOUT, "You are not logged yet!");
    }

    private void addInvalidCommand(ConsoleCommand command, String claim) {
        invalidCommandMap.put(command, claim);
    }
}
