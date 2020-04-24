package com.tianyuan.easyui.cmdclient.console;

import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;
import com.tianyuan.easyui.cmdclient.chat.ChatContext;
import com.tianyuan.easyui.cmdclient.chat.ChatType;
import com.tianyuan.easyui.cmdclient.chat.ClientStatus;
import com.tianyuan.easyui.cmdclient.chat.handler.ChatHandler;
import com.tianyuan.easyui.cmdclient.chat.handler.GroupChatHandler;
import com.tianyuan.easyui.cmdclient.chat.handler.P2PChatHandler;
import com.tianyuan.easyui.cmdclient.console.login.LoginConsole;
import com.tianyuan.easyui.cmdclient.console.login.LogoutConsole;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommandConsoleManager {

    private final Map<ConsoleCommand, CmdConsole> consoleMap;
    
    private final Map<ChatType, ChatHandler> chatMsgHandlerMap;

    private final ChatContext chatContext;
    
    public CommandConsoleManager(ChatContext chatContext) {
        consoleMap = initCmdConsoles(chatContext);
        chatMsgHandlerMap = initChatHandlers(chatContext);
        this.chatContext = chatContext;
    }
    
    public void cmdLoop() {
        System.out.println("Please use these commands to do operation: " + ConsoleCommand.allValuesStr());
        Scanner scanner = new Scanner(System.in);
        scanner.useDelimiter("\n");
        while (!chatContext.quited()) {
            String input = scanner.next();
            if (StringUtils.isBlank(input)) {
                continue;
            }
            try {
                if (ConsoleCommand.isSystemCmd(input)) {
                    // begin a system command console
                    execCmd(input, scanner);
                } else {
                    // begin to send chat message
                    execChatMsg(input);
                }
            } catch (Exception e) {
                log.error("Failed to execute the input:{}", input, e);
            }
        }
    }
    
    private void execChatMsg(String input) {
        ChatType chatType = ChatType.getChatType(input);
        if (chatType != null && chatContext.getStatus().validChatStatus()) {
            chatMsgHandlerMap.get(chatType).execMsg(input);
        }
    }

    private void execCmd(String input, Scanner scanner) {
        ConsoleCommand command = ConsoleCommand.getCommand(input);
        if (!chatContext.getStatus().isValid(command)) {
            return;
        }
        if (ConsoleCommand.QUIT.equals(command)) {
            chatContext.setStatus(ClientStatus.QUITTED);
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
        builder.put(ConsoleCommand.LOGOUT, new LogoutConsole(chatContext));
        builder.put(ConsoleCommand.ONLINE_USER_LIST, new GetUserListConsole(chatContext));
        return builder.build();
    }
    
    private Map<ChatType, ChatHandler> initChatHandlers(ChatContext chatContext) {
        ImmutableMap.Builder<ChatType, ChatHandler> builder = ImmutableMap.builder();
        builder.put(ChatType.P2P, new P2PChatHandler(chatContext));
        builder.put(ChatType.GROUP, new GroupChatHandler(chatContext));
        return builder.build();
    }
}
