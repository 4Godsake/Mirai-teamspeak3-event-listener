package cn.rapdog.ts3eventlistener;

import cn.rapdog.ts3eventlistener.command.TsCommand;
import cn.rapdog.ts3eventlistener.teamspeak.TsListener;
import cn.rapdog.ts3eventlistener.utils.LoadConfigFile;
import net.mamoe.mirai.console.command.Command;
import net.mamoe.mirai.console.command.CommandManager;
import net.mamoe.mirai.console.plugin.jvm.JavaPlugin;
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescriptionBuilder;

/**
 * @author rapdog
 */
public final class Ts3EventListenerPlugin extends JavaPlugin {

    public static final Ts3EventListenerPlugin INSTANCE = new Ts3EventListenerPlugin();

    private Ts3EventListenerPlugin() {
        super(new JvmPluginDescriptionBuilder("cn.rapdog.mirai-teamspeak3-event-listener", "0.1.0")
                .name("Teamspeak3 Event Listener")
                .info("一个mirai插件，用来监听teamspeak3服务器的事件，如用户进入频道、退出频道等，并将这些事件播报到指定的群聊中")
                .author("rapdog")
                .build());
    }

    @Override
    public void onEnable() {
        getLogger().info("configPath:" + getConfigFolderPath());
        getLogger().info("dataPath:" + getDataFolderPath());
        LoadConfigFile.init();
        TsListener.startListen();
        getLogger().info("Plugin Ts3EventListenerPlugin loaded!");
        CommandManager.INSTANCE.registerCommand(TsCommand.INSTANCE, false);

        for (Command command : CommandManager.INSTANCE.getAllRegisteredCommands()) {
            getLogger().info(command.getDescription());
        }
    }

    @Override
    public void onDisable() {
        getLogger().info("Ts3EventListenerPlugin quiting...");
    }
}