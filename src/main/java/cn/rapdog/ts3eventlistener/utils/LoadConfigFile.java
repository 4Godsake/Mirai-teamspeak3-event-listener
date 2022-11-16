package cn.rapdog.ts3eventlistener.utils;

import cn.hutool.core.comparator.VersionComparator;
import cn.hutool.core.util.StrUtil;
import cn.rapdog.ts3eventlistener.Ts3EventListenerPlugin;
import cn.rapdog.ts3eventlistener.config.Config;
import cn.rapdog.ts3eventlistener.config.EventConfig;
import cn.rapdog.ts3eventlistener.config.GlobalData;
import cn.rapdog.ts3eventlistener.config.reader.YmlConfigHandler;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author rapdog
 */
public class LoadConfigFile {

    private static String path;

    public static void init() {
        try {
            path = Ts3EventListenerPlugin.INSTANCE.getConfigFolderPath().toString();
        } catch (ExceptionInInitializerError ignored) {
            path = "";
        }
        try {
            createConfigFile();
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadConfig() throws IOException {
        try (InputStream configFromResourceStream = Ts3EventListenerPlugin.INSTANCE.getResourceAsStream("config.yml")) {
            YmlConfigHandler ymlConfigHandler = new YmlConfigHandler(Config.class);
            File configFile = Ts3EventListenerPlugin.INSTANCE.resolveConfigFile("config.yml");
            if (!configFile.exists()) {
                Ts3EventListenerPlugin.INSTANCE.getLogger().info("configFile doesn't exist,create default config file...");
                if (configFromResourceStream != null) {
                    ymlConfigHandler.overwriteConfigFile(configFile, configFromResourceStream);
                } else {
                    Ts3EventListenerPlugin.INSTANCE.getLogger().error("default config file doesn't exist, please contact author");
                }
            }
            Config config = readConfig(configFile.getPath());
            Config configFromResource = ymlConfigHandler.readConfigFile(configFromResourceStream);
            // 判断版本，合并配置
            String userConfigVersion = config != null && StrUtil.isNotEmpty(config.getVersion()) ? config.getVersion() : "0";
            String defaultConfigVersion = configFromResource != null && StrUtil.isNotEmpty(configFromResource.getVersion()) ? configFromResource.getVersion() : "0";
            if (VersionComparator.INSTANCE.compare(userConfigVersion, defaultConfigVersion) < 0) {
                try (InputStream inputStream = Files.newInputStream(configFile.toPath())) {
                    //todo 合并有问题,注释无法合并，暂时直接替换
//                    Map<String, Object> configMap = ymlConfigHandler.mergeConfig(Ts3EventListenerPlugin.INSTANCE.getResourceAsStream("config.yml"),inputStream);
//                    ymlConfigHandler.dumpYml(configFile.getPath(),configMap);
                    dumpConfigByDefault(ymlConfigHandler, configFile);
                    Ts3EventListenerPlugin.INSTANCE.getLogger().warning("发现新版本配置文件，已替换配置文件为最新版本，请手动修改配置文件后使用");
                }
            }
            GlobalData.config = readConfig(configFile.getPath());
            if (GlobalData.config != null) {
                EventConfig clientJoin = GlobalData.config.getListenEvent().getClientJoin();
                clientJoin.setEventKeyList(buildTemplateKeyList(clientJoin));
                EventConfig clientLeave = GlobalData.config.getListenEvent().getClientLeave();
                clientLeave.setEventKeyList(buildTemplateKeyList(clientLeave));
            }
        } catch (IOException ex) {
            Ts3EventListenerPlugin.INSTANCE.getLogger().error("读取默认配置失败", ex);
            throw new RuntimeException("teamspeak3-event-listener配置文件读取失败！！！", ex);
        }
    }


    private static List<String> buildTemplateKeyList(EventConfig ec) {
        List<String> keyList = new ArrayList<>();
        ec.getNoticeTemplate().forEach(tmp -> keyList.addAll(RegxUtil.getKeyListByContent(tmp)));
        return keyList;
    }

    private static void createConfigFile() {
        File mkdir = new File(path);
        if (!mkdir.exists()) {
            Ts3EventListenerPlugin.INSTANCE.getLogger().info(path + "不存在");
            mkdir.mkdirs();
        }
    }


    private static Config readConfig(String configPath) {
        YmlConfigHandler ymlConfigHandler = new YmlConfigHandler(Config.class);
        try {
            return ymlConfigHandler.readConfigFile(configPath);
        } catch (Exception e) {
            e.printStackTrace();
            Ts3EventListenerPlugin.INSTANCE.getLogger().error("读取用户配置失败，使用默认配置覆写用户配置，请修改配置后重启工程", e);
            dumpConfigByDefault(ymlConfigHandler, new File(configPath));
            return readConfigFromResource();
        }
    }

    private static void dumpConfigByDefault(YmlConfigHandler ymlConfigHandler, File configFile){
        try (InputStream inputStream = Ts3EventListenerPlugin.INSTANCE.getResourceAsStream("config.yml")) {
            ymlConfigHandler.overwriteConfigFile(configFile, inputStream);
        }catch (IOException ioe){
            Ts3EventListenerPlugin.INSTANCE.getLogger().error("用默认配置覆写用户配置失败", ioe);
            throw new RuntimeException("teamspeak3-event-listener配置文件覆写失败！！！", ioe);
        }
    }

    private static Config readConfigFromResource() {
        YmlConfigHandler ymlConfigHandler = new YmlConfigHandler(Config.class);
        try (InputStream inputStream = Ts3EventListenerPlugin.INSTANCE.getResourceAsStream("config.yml")) {
            return ymlConfigHandler.readConfigFile(inputStream);
        } catch (IOException ex) {
            Ts3EventListenerPlugin.INSTANCE.getLogger().error("读取默认配置失败", ex);
            throw new RuntimeException("teamspeak3-event-listener配置文件读取失败！！！", ex);
        }
    }

}
