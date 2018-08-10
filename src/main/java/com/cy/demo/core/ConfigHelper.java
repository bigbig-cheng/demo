package com.cy.demo.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;


/**
 * ConfigHelper
 */
public class ConfigHelper {

    // 本地资源文件配置
    public static final Properties localProperties = loadProperties("config/default.properties");

    static {
        // 加载额外配置文件
//        localProperties.putAll(loadProperties("config/extends.properties"));
    }

    public static String getString(String property, String _default) {
        String value = localProperties.getProperty(property, _default);
        if(value != null){
            try {
                return new String(value.getBytes("iso-8859-1"), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    public static String getString(String property) {
        return getString(property, null);
    }

    public static Integer getInt(String property, int defaultValue) {
        try {
            return Integer.parseInt(getString(property));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static Integer getInt(String property) {
        return getInt(property, 0);
    }

    public static Long getLong(String property) {
        return getLong(property);
    }

    public static Long getLong(String property, Long defaultValue) {
        try {
            return Long.parseLong(getString(property));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultValue;
    }

    public static boolean getBoolean(String property) {
        String value = getString(property);
        if(value==null || value.equals("")){
            return false;
        }
        return value.equalsIgnoreCase("true");
    }

    public static Boolean equals(String property, String value) {
        return getString(property).equals(value);
    }

    public static boolean isEmpty(String property) {
        return getString(property)==null||getString(property).equals("");
    }

    public static boolean isNotEmpty(String property) {
        return !isEmpty(property);
    }

    /**
     * 加载配置文件
     *
     * @param pattern
     * @return
     */
    public static Properties loadProperties(String pattern) {
        Properties result = new Properties();
        try {
            List<String> resources = ResourceHelper.getResources(pattern);
            for (String resource : resources) {
                InputStream inputStream = ResourceHelper.getInputStream(resource);
                Properties properties = new Properties();
                properties.load(inputStream);
                result.putAll(properties);
                inputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

}
