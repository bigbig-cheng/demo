package com.cy.demo.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * ResourceHelper
 */
public class ResourceHelper {
    private static final Pattern ignore = Pattern.compile("^(sun|java|javax|jdk|scala|redis|kafka|freemarker|schemaorg_apache_xmlbeans|com/(alibaba|google|mongodb|sun)|org/(apache|hibernate|springframework|codehaus|openxmlformats|eclipse|jboss|mockito|assertj|aspectj))");
    private static List<Map> mappings = new ArrayList();
    private static ClassLoader classLoader = ResourceHelper.class.getClassLoader();

    static {
        // 扫描所有潜在的 资源文件
        URL[] urls = ((URLClassLoader) (ResourceHelper.classLoader)).getURLs();
        for (URL url : urls) {
            try {
                recursionScan(new File(URLDecoder.decode(url.getFile(), "UTF-8")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // 递归读取 资源列表
    private static void recursionScan(File file) {
        try {
            if (file.isDirectory()) {
                for (File child : file.listFiles()) {
                    recursionScan(child);
                }
            } else if (file.getName().endsWith(".jar")) {
                if (file.exists()) {
                    for (ZipEntry entry : Collections.list(new ZipFile(file).entries())) {
                        String filepath = entry.getName().replace("\\", "/");
                        if (!filepath.endsWith("/") && !ignore.matcher(filepath).find()) {
                            Map<String, String> map = new HashMap<>();
                            map.put("name", filepath);
                            map.put("path", file.getAbsolutePath() + "!" + filepath);
                            mappings.add(map);
                        }
                    }
                }
            } else if (file.getAbsolutePath().indexOf("classes") != -1) {
                String filepath = file.getAbsolutePath().replace("\\", "/");
                String[] paths = filepath.split("classes");
                if (!ignore.matcher(paths[1].substring(1)).find()) {
                    Map<String, String> map = new HashMap<>();
                    map.put("name", paths[1].substring(1));
                    map.put("path", file.getAbsolutePath());
                    mappings.add(map);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 获取资源列表
     *
     * @param pattern
     * @return
     */
    public static List<String> getResources(String pattern) {
        List<String> paths = new ArrayList();
        try {
            for (Map<String, String> file : mappings) {
                String name = file.get("name");
                if (name.matches(pattern)) {
                    paths.add(file.get("path"));
                }
            }
            return paths;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return paths;
    }

    /**
     * 读取资源文件
     *
     * @param resource
     * @return
     */
    public static InputStream getInputStream(String resource) {
        if (resource.indexOf("!") == -1) {
            try {
                return new FileInputStream(new String(resource));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } else {
            String[] paths = resource.split("!");
            try {
                ZipFile zipFile = new ZipFile(paths[0]);
                return zipFile.getInputStream(zipFile.getEntry(paths[1]));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        List<String> files = ResourceHelper.getResources("config/default.properties");
        for (String file : files) {
            System.out.println(file);
        }
    }

    /**
     * 获取资源短名称
     *
     * @param resource
     * @return
     */
    public static String getResourceShortPath(String resource) {
        return resource.indexOf("classes") != -1 ? resource.split("classes")[1] : resource;
    }

}