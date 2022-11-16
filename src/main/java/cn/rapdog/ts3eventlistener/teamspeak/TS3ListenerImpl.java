package cn.rapdog.ts3eventlistener.teamspeak;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.rapdog.ts3eventlistener.Ts3EventListenerPlugin;
import cn.rapdog.ts3eventlistener.config.EventConfig;
import cn.rapdog.ts3eventlistener.config.GlobalData;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.ClientProperty;
import com.github.theholywaffle.teamspeak3.api.event.*;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import com.github.theholywaffle.teamspeak3.api.wrapper.ClientInfo;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.*;
import java.util.stream.Collectors;

import static cn.rapdog.ts3eventlistener.constant.TsConstants.CLIENT_NICKNAME;

/**
 * @author rapdog
 */
public class TS3ListenerImpl implements TS3Listener {

    private static final MiraiLogger LOGGER = Ts3EventListenerPlugin.INSTANCE.getLogger();

    public TS3ListenerImpl(String botName) {
        this.botName = botName;
    }

    private final String botName;

    @Override
    public void onTextMessage(TextMessageEvent e) {
        LOGGER.info("Text message received in " + e.getTargetMode());
    }

    @Override
    public void onServerEdit(ServerEditedEvent e) {
        LOGGER.info("Server edited by " + e.getInvokerName());
    }

    @Override
    public void onClientMoved(ClientMovedEvent e) {
        LOGGER.info("Client has been moved " + e.getClientId());
    }

    @Override
    public void onClientJoin(ClientJoinEvent e) {
        // 因serverQuery登陆时也算作一个用户，需将该用户名排除
        if (GlobalData.config.getListenEvent().getClientJoin().isEnable() && !e.get(CLIENT_NICKNAME).contains(botName)) {
            LOGGER.info("接收到客户端加入事件：" + e);
            final TS3Api api = TsApiConnection.getInstance().getApi();
            ClientInfo client = api.getClientInfo(e.getClientId());
            LOGGER.info("完整用户信息" + client);
            eventNotice(e, client, GlobalData.config.getListenEvent().getClientJoin());
            TsClientWrapper.getInstance().addClient(client);
        }
    }

    @Override
    public void onClientLeave(ClientLeaveEvent e) {
        Client client = TsClientWrapper.getInstance().getClientById(e.getClientId());
        if (client == null) {
            LOGGER.info("离开的用户信息获取失败");
            return;
        }
        if (GlobalData.config.getListenEvent().getClientLeave().isEnable() && !client.getNickname().contains(botName)) {
            LOGGER.info("接收到客户端离开事件：" + e);
            LOGGER.info("完整用户信息" + client);
            eventNotice(e, client, GlobalData.config.getListenEvent().getClientLeave());
            TsClientWrapper.getInstance().removeClientById(e.getClientId());
        }
    }

    @Override
    public void onChannelEdit(ChannelEditedEvent e) {
        // ...
    }

    @Override
    public void onChannelDescriptionChanged(ChannelDescriptionEditedEvent e) {
        // ...
    }

    @Override
    public void onChannelCreate(ChannelCreateEvent e) {
        // ...
    }

    @Override
    public void onChannelDeleted(ChannelDeletedEvent e) {
        // ...
    }

    @Override
    public void onChannelMoved(ChannelMovedEvent e) {
        // ...
    }

    @Override
    public void onChannelPasswordChanged(ChannelPasswordChangedEvent e) {
        // ...
    }

    @Override
    public void onPrivilegeKeyUsed(PrivilegeKeyUsedEvent e) {
        // ...
    }

    private void eventNotice(BaseEvent e, Client c, EventConfig config) {
        Bot bot = Bot.getInstances().get(0);
        String enableGroups = Optional.ofNullable(config.getNoticeGroups()).orElse("");
        String enableFriends = Optional.ofNullable(config.getNoticeFriends()).orElse("");
        List<String> noticeTemplate = Optional.ofNullable(config.getNoticeTemplate()).orElse(new ArrayList<>());
        List<String> eventKeyList = Optional.ofNullable(config.getEventKeyList()).orElse(ListUtil.empty());
        String message = buildMessage(e, c, noticeTemplate, eventKeyList);
        if (StrUtil.isNotEmpty(enableGroups)) {
            List<Long> noticeGroupIdList = Arrays.stream(enableGroups.split(",")).map(this::parseLong).collect(Collectors.toList());
            noticeGroupIdList.forEach(id -> Objects.requireNonNull(bot.getGroup(id)).sendMessage(message));
        }
        if (StrUtil.isNotEmpty(enableFriends)) {
            List<Long> noticeFriendIdList = Arrays.stream(enableFriends.split(",")).map(this::parseLong).collect(Collectors.toList());
            noticeFriendIdList.forEach(id -> Objects.requireNonNull(bot.getFriend(id)).sendMessage(message));
        }
    }

    private Long parseLong(String s) {
        try {
            return Long.parseLong(s.trim());
        } catch (NumberFormatException nfe) {
            LOGGER.error("【" + s + "】不是一个正确的号码，请检查配置文件");
            return null;
        }
    }

    private String buildMessage(BaseEvent e, Client c, List<String> templateList, List<String> eventKeys) {
        String template = getRandomTemplate(templateList);
        for (ClientProperty prop : ClientProperty.values()) {
            String value = Optional.ofNullable(c.get(prop)).orElse("NULL");
            template = StrUtil.replaceIgnoreCase(template, "${client." + prop.getName() + "}", value);
        }
        for (String eventKey : eventKeys) {
            String value = Optional.ofNullable(e.get(eventKey)).orElse("NULL");
            template = StrUtil.replaceIgnoreCase(template, "${event." + eventKey + "}", value);
        }
        return template;
    }

    private String getRandomTemplate(List<String> templateList){
        if (CollUtil.isNotEmpty(templateList)){
            int index = new Random().nextInt(templateList.size());
            return templateList.get(index);
        }
        return "未配置消息模板";
    }
}
