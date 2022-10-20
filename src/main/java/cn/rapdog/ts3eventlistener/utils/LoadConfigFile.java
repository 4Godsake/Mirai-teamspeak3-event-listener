package cn.rapdog.ts3eventlistener.utils;

import cn.rapdog.ts3eventlistener.Ts3EventListenerPlugin;
import cn.rapdog.ts3eventlistener.config.Config;
import cn.rapdog.ts3eventlistener.config.EventConfig;
import cn.rapdog.ts3eventlistener.config.GlobalData;
import com.alibaba.fastjson.JSON;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
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
            createProjectFile();
            loadConfig();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadConfig() throws IOException {
        File configFile = Ts3EventListenerPlugin.INSTANCE.resolveConfigFile("config.yml");
        if (!configFile.exists()) {
            Ts3EventListenerPlugin.INSTANCE.getLogger().info("configFile doesn't exist,create default config file...");
            InputStream inputStream = Ts3EventListenerPlugin.INSTANCE.getResourceAsStream("config.yml");
            if (inputStream != null) {
                fileOut(configFile, inputStream);
            } else {
                Ts3EventListenerPlugin.INSTANCE.getLogger().error("default config file doesn't exist, please contact author");
            }
        }
        GlobalData.config = readConfig(configFile.getPath());
        if (GlobalData.config != null){
            EventConfig clientJoin = GlobalData.config.getListenEvent().getClientJoin();
            clientJoin.setEventKeyList(RegxUtil.getKeyListByContent(clientJoin.getNoticeTemplate()));
            EventConfig clientLeave = GlobalData.config.getListenEvent().getClientLeave();
            clientLeave.setEventKeyList(RegxUtil.getKeyListByContent(clientLeave.getNoticeTemplate()));
        }
    }

    public static void createProjectFile() {
        File mkdir = new File(path);
        if (!mkdir.exists()) {
            Ts3EventListenerPlugin.INSTANCE.getLogger().info(path+"不存在");
            mkdir.mkdirs();
        }
    }

    public static void fileOut(File file, InputStream inputStream) throws IOException {
        try (OutputStream output = Files.newOutputStream(file.toPath())) {
            byte[] bytes = new byte[1024];
            while (true) {
                int readLength = inputStream.read(bytes);
                if (readLength == -1) {
                    break;
                }
                byte[] outData = new byte[readLength];
                System.arraycopy(bytes, 0, outData, 0, readLength);
                output.write(outData);
            }
            output.flush();
            inputStream.close();
        }
    }


    public static Config readConfig(String configPath) {
        try (InputStream inputStream = Files.newInputStream(Paths.get(configPath))) {
            return yml2Config(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            Ts3EventListenerPlugin.INSTANCE.getLogger().error("读取用户配置失败", e);
            try {
                return readDefaultConfig();
            } catch (IOException ex) {
                Ts3EventListenerPlugin.INSTANCE.getLogger().error("读取默认配置失败", ex);
                return null;
            }
        }
    }

    private static Config readDefaultConfig() throws IOException {
        try (InputStream inputStream = Ts3EventListenerPlugin.INSTANCE.getResourceAsStream("config.yml")) {
            return yml2Config(inputStream);
        }

    }

    private static Config yml2Config(InputStream is){
        Yaml yaml = new Yaml();
        Map<String, Object> map = yaml.load(is);
        Ts3EventListenerPlugin.INSTANCE.getLogger().info("读取配置map为" + map);
        return JSON.parseObject(JSON.toJSONString(map), Config.class);
    }

}
