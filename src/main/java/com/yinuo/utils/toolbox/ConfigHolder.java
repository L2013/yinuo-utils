package com.yinuo.utils.toolbox;

import com.yinuo.utils.security.ThreeDES;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author liang
 */
public class ConfigHolder {
    private static Logger logger = LoggerFactory.getLogger(ConfigHolder.class);
    private static Properties prop = new Properties();

    public static String md5Key = "";

    private ConfigHolder() {
    }

    public static Properties getProp() {
        return prop;
    }

    public static void load(String applicationPath) {
        try {
            File f = new File(applicationPath);
            prop.load(new FileReader(f));
            md5Key = prop.getProperty("md5.key");
            decodeProperties(prop);
        } catch (IOException e) {
            logger.error("fail to load properties!\n{}" + e);
        }
    }

    public static void loadResource(String resourcePath) {
        try {
            ClassLoader classLoader = ConfigHolder.class.getClassLoader();
            prop.load(classLoader.getResourceAsStream(resourcePath));
            md5Key = prop.getProperty("md5.key");
            decodeProperties(prop);
        } catch (IOException e) {
            logger.error("fail to load properties!\n{}" + e);
        }
    }

    public static boolean isEncrypted(String strKey, String strVal) {
        if (strVal == null) {
            return false;
        }
        return strVal.startsWith("ENC(") && strVal.endsWith(")");
    }

    public static void decodeProperties(Properties prop) {
        List<Object> fields = new ArrayList<>();
        for (Object key : prop.keySet()) {
            String strKey = String.valueOf(key).toLowerCase();
            String strVal = prop.getProperty(strKey);
            if (isEncrypted(strKey, strVal)) {
                fields.add(key);
            }
        }
        for (Object f : fields) {
            String rawValue = prop.getProperty(String.valueOf(f));
            String newValue = ThreeDES.decode(md5Key, rawValue.substring(4, rawValue.length() - 1));
            prop.put(f, newValue);
        }
    }

    public static String get(String key) {
        return prop.getProperty(key);
    }
}
