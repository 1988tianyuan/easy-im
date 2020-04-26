package com.tianyuan.easyim.common.protocal;

import static com.tianyuan.easyim.common.model.IMMsg.*;
import static com.tianyuan.easyim.common.model.IMMsg.RequestType.*;

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.protobuf.Any;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import lombok.NonNull;

public class ChatMsgUtil {

    private static final Map<RequestType, Class<? extends Message>> requestMsgMap;

    static {
        Map<RequestType, Class<? extends Message>> map = new HashMap<>();
        map.put(ChatRequest, ChatRequestMsg.class);
        map.put(ChatResponse, ChatResponseMsg.class);
        map.put(CreateSessionRequest, SessionCreateRequestMsg.class);
        map.put(CreateSessionResponse, SessionCreateResponseMsg.class);
        map.put(FinishSessionRequest, FinishSessionRequestMsg.class);
        requestMsgMap = Maps.immutableEnumMap(map);
    }

    public static BaseRequestMsg createBaseMsg(@NonNull RequestType requestType, @NonNull Message msg) {
        validRequestMsg(requestType, msg);
        return BaseRequestMsg.newBuilder()
            .setRequestType(requestType)
            .setRequestMsg(Any.pack(msg))
            .build();
    }

    public static Message parseRequestMsg(@NonNull BaseRequestMsg baseRequestMsg) throws InvalidProtocolBufferException {
        RequestType requestType = baseRequestMsg.getRequestType();
        Preconditions.checkNotNull(requestType, "requestType should not be null!");
        Any anyMsg = baseRequestMsg.getRequestMsg();
        Preconditions.checkNotNull(anyMsg, "inner msg in baseRequestMsg should not be null!");
        Class<? extends Message> requestMsgClass = requestMsgMap.get(requestType);
        Preconditions.checkArgument(anyMsg.is(requestMsgClass),
                "When RequestType is " + requestType + ", msg's Class should be " + requestMsgClass);
        return anyMsg.unpack(requestMsgMap.get(requestType));
    }

    public static void validRequestMsg(RequestType requestType, Message msg) {
        switch (requestType) {
            case ChatRequest:
                Preconditions.checkArgument(msg instanceof ChatRequestMsg,
                        "When RequestType is ChatRequest, msg should be ChatRequestMsg!");
                break;
            case ChatResponse:
                Preconditions.checkArgument(msg instanceof ChatResponseMsg,
                        "When RequestType is ChatResponse, msg should be ChatRequestMsg!");
                break;
            case CreateSessionRequest:
                Preconditions.checkArgument(msg instanceof SessionCreateRequestMsg,
                        "When RequestType is CreateSession, msg should be SessionCreateResponseMsg!");
                break;
            case CreateSessionResponse:
                Preconditions.checkArgument(msg instanceof SessionCreateResponseMsg,
                    "When RequestType is CreateSessionRequest, msg should be CreateSessionResponse!");
                break;
            case FinishSessionRequest:
                Preconditions.checkArgument(msg instanceof FinishSessionRequestMsg,
                    "When RequestType is FinishSessionRequest, msg should be FinishSessionRequestMsg!");
            default:
                throw new IllegalArgumentException("Unknown requestType:" + requestType + ", please confirm.");
        }
    }
}
