package cn.rapdog.ts3eventlistener.config.reader;


import cn.rapdog.ts3eventlistener.config.BaseConfig;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;

/**
 * @author rapdog
 */
public interface ConfigHandler {

    /**
     * 读取配置文件
     * @param is InputStream
     * @return T
     * @param <T> 配置文件类,需继承BaseConfig
     * @throws IOException ioe
     */
    <T extends BaseConfig> T readConfigFile(InputStream is) throws IOException;

    /**
     * 读取配置文件
     * @param filePath 文件路径
     * @return T
     * @param <T> 配置文件类，需继承BaseConfig
     * @throws IOException ioe
     */
    <T extends BaseConfig> T readConfigFile(String filePath) throws IOException;

    /**
     * 覆写配置
     * @param configFile 目标文件
     * @param inputStream 源is
     */
    default void overwriteConfigFile(File configFile, InputStream inputStream){
        try (OutputStream output = Files.newOutputStream(configFile.toPath())) {
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
        } catch (IOException e) {
            throw new RuntimeException("覆写配置文件失败",e);
        }
    }
}
