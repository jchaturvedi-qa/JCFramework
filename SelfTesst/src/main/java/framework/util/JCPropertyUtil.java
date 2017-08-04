package framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by jchaturvedi on 12-07-2017.
 */
public class JCPropertyUtil {

    private static JCPropertyUtil prop;
    private Properties properties;

    private JCPropertyUtil() {
        properties = new Properties();
    }

    public static synchronized JCPropertyUtil getInstance() {
        if (prop == null) {
            prop = new JCPropertyUtil();
        }
        return prop;
    }

    public void load(String fileName) throws IOException {
        InputStream input = null;
        input = getClass().getClassLoader().getResourceAsStream(fileName);
        properties.load(input);
    }

    public void load(File file) throws IOException {
        InputStream input = new FileInputStream(file);
        properties.load(input);
    }

    public void clear() {
        properties.clear();
    }

    public String getValue(String key) {
        return properties.getProperty(key).trim();
    }

    public void setValue(String key, String value) {
        properties.setProperty(key, value);
    }
}
