package com.tianyuan.easyui.cmdclient.chat;

import static com.tianyuan.easyui.cmdclient.console.ConsoleCommand.*;

import java.util.EnumMap;

import com.tianyuan.easyui.cmdclient.console.ConsoleCommand;

public enum ClientStatus {
    INIT, LOGGED, QUIT;

    private EnumMap<ConsoleCommand, String> invalidCommandMap = new EnumMap<>(ConsoleCommand.class);

    public boolean isValid(ConsoleCommand command) {
        if (invalidCommandMap.containsKey(command)) {
            System.out.println(invalidCommandMap.get(command));
            return false;
        }
        return true;
    }
    
    public boolean validChatStatus() {
        if (!LOGGED.equals(this)) {
            System.out.println("You should login firstly before chat.");
            return false;
        }
        return true;
    }

    static  {
        LOGGED.addInvalidCommand(LOGIN, "You have already logged, please logout firstly.");
        INIT.addInvalidCommand(ConsoleCommand.LOGOUT, "You are not logged yet!");
    }

    private void addInvalidCommand(ConsoleCommand command, String claim) {
        invalidCommandMap.put(command, claim);
    }
}
