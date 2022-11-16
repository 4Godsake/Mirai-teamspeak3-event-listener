package cn.rapdog.ts3eventlistener.config;

import lombok.Data;

import java.util.List;

/**
 * @author rapdog
 */
@Data
public class EventConfig{

    private boolean enable;

    private String noticeGroups;

    private String noticeFriends;

    private List<String> noticeTemplate;

    private List<String> eventKeyList;
}
