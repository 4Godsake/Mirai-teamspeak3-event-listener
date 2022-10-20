package cn.rapdog.ts3eventlistener.command;

import cn.rapdog.ts3eventlistener.Ts3EventListenerPlugin;
import cn.rapdog.ts3eventlistener.teamspeak.TsClientInfoQuery;
import cn.rapdog.ts3eventlistener.utils.QpsControlUtil;
import net.mamoe.mirai.console.command.CommandSender;
import net.mamoe.mirai.console.command.java.JRawCommand;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.utils.MiraiLogger;
import org.jetbrains.annotations.NotNull;


/**
 * @author rapdog
 */
public final class TsCommand extends JRawCommand {
    public static final TsCommand INSTANCE = new TsCommand();

    private static final MiraiLogger LOGGER = Ts3EventListenerPlugin.INSTANCE.getLogger();

    private TsCommand() {
        super(Ts3EventListenerPlugin.INSTANCE, "ts");
        // 可选设置如下属性
        this.setUsage("/ts");
        this.setDescription("获取teamspeak服务器在线成员清单");
        this.setPrefixOptional(true);
    }

    @Override
    public void onCommand(@NotNull CommandSender sender, @NotNull MessageChain args) {
        // 处理指令
        String result = QpsControlUtil.getInstance().isPass() ? TsClientInfoQuery.getClients() : "请求过于频繁，请稍后再试";
        if (sender.getSubject() != null) {
            sender.getSubject().sendMessage(result);
        } else {
            LOGGER.info(result);
        }
    }
}