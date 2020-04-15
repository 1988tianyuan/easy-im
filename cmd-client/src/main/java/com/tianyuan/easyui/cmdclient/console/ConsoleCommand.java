package com.tianyuan.easyui.cmdclient.console;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.ImmutableMap;

public enum ConsoleCommand {
    LOGOUT("&logout"), LOGIN("&login"), QUIT("&quit");

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

    public static ConsoleCommand getCommand(String cmdStr) {
        return cmdMap.get(cmdStr);
    }
    
    public static String allValuesStr() {
        return StringUtils.join(cmdMap.keySet(), " | ");
    }
    
    public static String valuesStr(Set<ConsoleCommand> commands) {
        if (CollectionUtils.isEmpty(commands)) {
            return "no commands";
        }
        return StringUtils.join(commands.stream().map(ConsoleCommand::getValue).collect(Collectors.toList()), " | ");
    }
    
    public static boolean isSystemCmd(String input) {
        if (input.startsWith("&")) {
            if (cmdMap.containsKey(input)) {
                return true;
            }
            System.out.println("Please use these commands to do operation: " + ConsoleCommand.allValuesStr());
        }
        return false;
    }
    
    public String getValue() {
        return value;
    }
}
