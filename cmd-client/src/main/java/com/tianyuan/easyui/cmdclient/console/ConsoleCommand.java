package com.tianyuan.easyui.cmdclient.console;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

public enum ConsoleCommand {
    CHAT_WITH_USER("&chat"), LOGOUT("&logout"), LOGIN("&login"), QUIT("&quit");

    private static Map<String, ConsoleCommand> cmdMap;

    static {
        ImmutableMap.Builder<String, ConsoleCommand> builder = ImmutableMap.builder();
        for (ConsoleCommand command : values()) {
            builder.put(command.value, command);
        }
        cmdMap = builder.build();
    }

    private String value;

    ConsoleCommand(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ConsoleCommand getCommand(String cmdStr) {
        return cmdMap.get(cmdStr);
    }
    
    public static String allValuesStr() {
        return StringUtils.join(cmdMap.keySet(), ", ");
    }
}
