package cn.rapdog.ts3eventlistener.config;

import lombok.Data;

/**
 * @author rapdog
 */
@Data
public class ListenEvent {

    private EventConfig clientJoin;

    private EventConfig clientLeave;
}