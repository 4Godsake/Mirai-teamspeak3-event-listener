package cn.rapdog.ts3eventlistener.config;

import lombok.Data;

/**
 * @author rapdog
 */
@Data
public class Config {

    private String host;

    private String account;

    private String password;

    private String ignoreBotName;

    private ListenEvent listenEvent;

}
