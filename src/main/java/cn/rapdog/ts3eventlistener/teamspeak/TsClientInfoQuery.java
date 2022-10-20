package cn.rapdog.ts3eventlistener.teamspeak;

import cn.rapdog.ts3eventlistener.Ts3EventListenerPlugin;
import cn.rapdog.ts3eventlistener.config.GlobalData;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.api.wrapper.Channel;
import com.github.theholywaffle.teamspeak3.api.wrapper.Client;
import net.mamoe.mirai.utils.MiraiLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rapdog
 */
public class TsClientInfoQuery {

    private static final MiraiLogger LOGGER = Ts3EventListenerPlugin.INSTANCE.getLogger();

    public static String getClients() {
        String botName = GlobalData.config.getIgnoreBotName();
        final TS3Api api = TsApiConnection.getInstance().getApi();
        List<Channel> channels = api.getChannels();
        List<Client> clientList = api.getClients();
        Map<Integer, Channel> channelMap = new HashMap<>(channels.size());
        for (Channel channel : channels) {
            channelMap.put(channel.getId(), channel);
        }
        StringBuilder resp = new StringBuilder("当前TS在线人数：${num}");
        int num = clientList.size();

        // List all clients in the console
        for (Client c : clientList) {
            // Get the client's channel
            Channel channel = channelMap.get(c.getChannelId());

            // Write the client and channel name into the console
            if (!c.getNickname().contains(botName)) {
                resp.append("\n").append(c.getNickname()).append(" in channel ").append(channel.getName());
            } else {
                num = num - 1;
            }
        }
        return resp.toString().replace("${num}", String.valueOf(num));
    }
}
