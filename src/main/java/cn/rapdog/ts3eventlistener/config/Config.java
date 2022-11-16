package cn.rapdog.ts3eventlistener.config;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author rapdog
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Config extends BaseConfig{

    private String host;

    private String account;

    private String password;

    private String ignoreBotName;

    private ListenEvent listenEvent;

}
