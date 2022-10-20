package cn.rapdog.ts3eventlistener.teamspeak;

import com.github.theholywaffle.teamspeak3.api.wrapper.Client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rapdog
 */
public class TsClientWrapper {

    public TsClientWrapper(){
        clients = new HashMap<>();
    }

    private Map<Integer, Client> clients;

    private static class SingletonHolder {
        private static final TsClientWrapper SINGLETON = new TsClientWrapper();
    }

    /**
     * 单例方法入口
     * @return 单例类
     */
    public static TsClientWrapper getInstance() {
        return TsClientWrapper.SingletonHolder.SINGLETON;
    }

    public Client getClientById(Integer clientId){
        return clients.get(clientId);
    }

    public void addClient(Client client){
        clients.put(client.getId(),client);
    }

    public void removeClientById(Integer clientId){
        clients.remove(clientId);
    }

    public void addAll(List<Client> clientList){
        clientList.forEach(c -> clients.put(c.getId(),c));
    }
}
