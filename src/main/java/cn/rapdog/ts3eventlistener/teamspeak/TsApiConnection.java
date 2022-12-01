package cn.rapdog.ts3eventlistener.teamspeak;


import cn.rapdog.ts3eventlistener.Ts3EventListenerPlugin;
import cn.rapdog.ts3eventlistener.config.GlobalData;
import com.github.theholywaffle.teamspeak3.TS3Api;
import com.github.theholywaffle.teamspeak3.TS3Config;
import com.github.theholywaffle.teamspeak3.TS3Query;
import com.github.theholywaffle.teamspeak3.api.exception.TS3ConnectionFailedException;

import java.util.List;

/**
 * @author rapdog
 */
public class TsApiConnection {

    private static class SingletonHolder {
        private static final TsApiConnection SINGLETON = new TsApiConnection();
    }

    /**
     * 单例方法入口
     * @return 单例类
     */
    public static TsApiConnection getInstance() {
        return TsApiConnection.SingletonHolder.SINGLETON;
    }

    private TS3Api api;

    private void getConnection(){
        String host = GlobalData.config.getHost();
        int port = GlobalData.config.getPort();
        String serverAdmin = GlobalData.config.getAccount();
        String password = GlobalData.config.getPassword();
        try {
            final TS3Config config = new TS3Config();
            config.setHost(host);
            config.setQueryPort(port);
            config.setEnableCommunicationsLogging(true);
            final TS3Query query = new TS3Query(config);
            Ts3EventListenerPlugin.INSTANCE.getLogger().info("TS3Query is connecting...");
            query.connect();
            api = query.getApi();
            api.login(serverAdmin, password);
            api.selectVirtualServerById(1);
        } catch (TS3ConnectionFailedException cfe) {
            Ts3EventListenerPlugin.INSTANCE.getLogger().error("tsServer listener not start,could not connect to ts3 server[" + host + "]");
        }
    }

    public TS3Api getApi(){
        if (api == null) {
            getConnection();
        }
        return api;
    }
}
