package org.tafia.tools.application;

import com.alibaba.fastjson.JSON;
import org.tafia.tools.utils.Exceptions;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Created by Dason on 2018/4/20.
 */
public class Global {

    private static final String CONFIG_FILE_NAME = "application.yaml";

    private static GlobalConfig globalConfig;

    private static Map<String, Object> globalData;

    private static Path codeSource;

    private static void initializeConfig() {
        InputStream inputStream = Global.class.getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
        if (inputStream == null) {
            globalConfig = new GlobalConfig();
        } else {
            Yaml yaml = new Yaml();
            globalConfig = yaml.loadAs(inputStream, GlobalConfig.class);
        }
    }

    private static void initializeData() {
        InputStream inputStream = Global.class.getClassLoader().getResourceAsStream(getGlobalConfig().getDataFile());
        if (inputStream == null) {
            globalData = new HashMap<>();
        } else {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String content = reader.lines().collect(Collectors.joining());
                globalData = new ConcurrentHashMap<>(JSON.parseObject(content));
            } catch (IOException e) {
                throw Exceptions.checked(e);
            }
        }
    }

    public static GlobalConfig getGlobalConfig() {
        if (globalConfig == null) {
            synchronized (Global.class) {
                if (globalConfig == null) {
                    initializeConfig();
                }
            }
        }
        return globalConfig;
    }

    public static Object getData(String key) {
        if (globalData == null) {
            synchronized (Global.class) {
                if (globalData == null) {
                    initializeData();
                }
            }
        }
        return globalData.get(key);
    }

    public static void setData(String key, Object value) {
        globalData.put(key, value);
        if (codeSource().toFile().isDirectory()) {
            Path dataPath = codeSource.resolve(getGlobalConfig().getDataFile());
            writeData(dataPath, globalData);
        } else {
            Path originDataPath = Paths.get(System.getProperty(getGlobalConfig().getOriginJarKey()));
            try (FileSystem fs = FileSystems.newFileSystem(originDataPath, Global.class.getClassLoader())) {
                Path dataPath = fs.getPath(getGlobalConfig().getDataFile());
                writeData(dataPath, globalData);
            } catch (IOException e) {
                throw Exceptions.checked(e);
            }
        }
    }

    private static void writeData(Path dataPath, Map<String, Object> data) {
        byte[] bytes = JSON.toJSONBytes(data);
        try {
            Files.write(dataPath, bytes, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw Exceptions.checked(e);
        }
    }

    public static Path codeSource() {
        if (codeSource == null) {
            synchronized (Global.class) {
                if (codeSource == null) {
                    codeSource = new File(Global.class.getProtectionDomain().getCodeSource().getLocation().getPath()).toPath();
                }
            }
        }
        return codeSource;
    }
}
