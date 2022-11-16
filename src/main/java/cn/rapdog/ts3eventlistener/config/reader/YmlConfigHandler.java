package cn.rapdog.ts3eventlistener.config.reader;

import cn.rapdog.ts3eventlistener.config.BaseConfig;
import org.springframework.beans.factory.config.YamlMapFactoryBean;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.core.io.InputStreamResource;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * @author rapdog
 */
public class YmlConfigHandler implements ConfigHandler {

    private final Class<? extends BaseConfig> clazz;

    public YmlConfigHandler(Class<? extends BaseConfig> clazz) {
        this.clazz = clazz;
    }

    @Override
    public <T extends BaseConfig> T readConfigFile(InputStream is) throws IOException {
        return yml2Config(is);
    }

    @Override
    public <T extends BaseConfig> T readConfigFile(String filePath) throws IOException {
        try (InputStream inputStream = Files.newInputStream(Paths.get(filePath))) {
            return readConfigFile(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @SuppressWarnings("unchecked")
    private <T extends BaseConfig> T yml2Config(InputStream is) {
        Yaml yaml = new Yaml();
        return (T) yaml.loadAs(is, clazz);
    }

    private Map<String, Object> yml2Map(InputStream is) {
        Yaml yaml = new Yaml();
        return yaml.load(is);
    }

    public Map<String, Object> mergeConfig(InputStream oldConfig, InputStream latestConfig) {
        YamlMapFactoryBean factory = new YamlMapFactoryBean();
        factory.setResolutionMethod(YamlProcessor.ResolutionMethod.OVERRIDE_AND_IGNORE);
        // 后面的会覆盖前面的
        factory.setResources(new InputStreamResource(latestConfig), new InputStreamResource(oldConfig));
        return factory.getObject();
    }

    public void dumpYml(String filePath, Map<String, Object> yamlValueMap) throws IOException {
        Yaml yaml = new Yaml();
        FileWriter fileWriter = new FileWriter(filePath);
        yaml.dump(yamlValueMap, fileWriter);
    }
}
