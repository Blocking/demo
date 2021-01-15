package com.example.server.netty;

import org.springframework.context.ApplicationEvent;

/**
 * @author zhangxiaoyu
 * @date 2021/1/13
 */
public class StartNettyServerEvent extends ApplicationEvent {
     /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public StartNettyServerEvent(Object source) {
        super(source);
    }
}
