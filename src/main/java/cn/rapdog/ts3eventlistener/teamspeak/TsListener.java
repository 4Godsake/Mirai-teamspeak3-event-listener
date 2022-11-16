package cn.rapdog.ts3eventlistener.teamspeak;

import cn.rapdog.ts3eventlistener.Ts3EventListenerPlugin;
import cn.rapdog.ts3eventlistener.config.GlobalData;
import com.github.theholywaffle.teamspeak3.*;

/**
 * @author rapdog
 */
public class TsListener {

    public static void startListen() {
        String botName = GlobalData.config.getIgnoreBotName();
        final TS3Api api = TsApiConnection.getInstance().getApi();
        if (api != null){
            api.registerAllEvents();
            api.addTS3Listeners(new TS3ListenerImpl(botName));
            TsClientWrapper.getInstance().addAll(api.getClients());
            Ts3EventListenerPlugin.INSTANCE.getLogger().info("tsServer listener started...");
        }else {
            Ts3EventListenerPlugin.INSTANCE.getLogger().error("fail to start tsServerListener, please check your teamspeak host...");
            throw new RuntimeException("teamspeak服务连接失败");
        }
    }
}
