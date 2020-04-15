package com.tianyuan.easyui.cmdclient.chat;

import static com.tianyuan.easyui.cmdclient.console.ConsoleCommand.*;

import java.text.MessageFormat;
import java.util.Collections;
import java.util.Set;

import com.google.common.collect.Sets;
import com.tianyuan.easyui.cmdclient.console.ConsoleCommand;

public enum ClientStatus {
    INIT(Sets.newHashSet(LOGIN, QUIT)),
    QUITTED(Collections.emptySet()),
    LOGGED_IN(Sets.newHashSet(LOGOUT, QUIT)) {
        @Override
        public boolean validChatStatus() {
            return true;
        }
    };
    
    protected Set<ConsoleCommand> supportCommands;
    
    ClientStatus(Set<ConsoleCommand> supportCommands) {
        this.supportCommands = supportCommands;
    }
    
    // default always reject chat
    public boolean validChatStatus() {
        System.out.println("You can't chat now, please login firstly!");
        return false;
    }
    
    public boolean isValid(ConsoleCommand command) {
        if (!supportCommands.contains(command)) {
            String warn = MessageFormat.format("Currently these commands are permitted: {0}, but your command is: {1}",
                ConsoleCommand.valuesStr(supportCommands), command.getValue());
            System.out.println(warn);
            return false;
        }
        return true;
    }
}
