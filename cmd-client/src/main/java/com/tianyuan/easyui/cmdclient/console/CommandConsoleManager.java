package com.tianyuan.easyui.cmdclient.console;

import com.google.common.collect.ImmutableMap;
import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.chat.ClientStatus;
import com.tianyuan.easyui.cmdclient.login.LoginConsole;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Scanner;

@Slf4j
public class CommandConsoleManager {

    private final Map<ConsoleCommand, CmdConsole> consoleMap;

    private final ChatContext chatContext;
    
    public CommandConsoleManager(ChatContext chatContext) {
        consoleMap = initCmdConsoles(chatContext);
        this.chatContext = chatContext;
    }
    
    public void cmdLoop() {
        System.out.println("Please enter these commands and begin your chat: " + ConsoleCommand.allValuesStr());
        Scanner scanner = new Scanner(System.in);
        while (!chatContext.quited()) {
            String input = scanner.next();
            if (StringUtils.isBlank(input)) {
                continue;
            }
            exec(input, scanner);
        }
    }

    private void exec(String input, Scanner scanner) {
        ConsoleCommand command = ConsoleCommand.getCommand(input);
        if (!chatContext.getStatus().isValid(command)) {
            return;
        }
        if (ConsoleCommand.QUIT.equals(command)) {
            chatContext.setStatus(ClientStatus.QUIT);
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
}
