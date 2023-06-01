package com.emily.infrastructure.file;

import java.io.*;
import java.net.URL;
import java.util.Properties;

/**
 * @Description :  文件资源读取操作
 * @Author :  Emily
 * @CreateDate :  Created in 2023/5/28 4:37 PM
 */
public class FileResourceUtils {
    public static final String FILE_PREFIX = "file://";
    public static final String HTTP_PREFIX = "http://";
    public static final String HTTPS_PREFIX = "https://";
    public static final String CLASSPATH_PREFIX = "classpath:";
    public static final String XML_SUFFIX = ".xml";

    /**
     * 读取指定路径下的文件，并将文件转换为key-value类型
     * -----------------------------------------------------------------------
     * 一、读取属性配置文件(properties、yaml类型)：
     * 读取classpath环境变量下的配置：classpath:test.properties
     * 读取绝对路径下的配置：/Users/xx/Documents/IDE/workplace-java/spring-parent/oceansky-file/src/test/resources/test.properties
     * 读取http获取https远程目录下的配置：http://www.xx.xxx/test.properties
     * 读取file路径下配置文件：file://a.ba/test.properties
     * 二、同样支持读取各种路径下的xml属性配置文件
     * -----------------------------------------------------------------------
     *
     * @param filePath 文件路径
     * @return 键值对类型配置文件
     */
    public static Properties loadConfig(String filePath) throws IOException {
        Properties properties = new Properties();
        InputStream inStream = null;
        try {
            boolean xml;
            if (filePath.startsWith(FILE_PREFIX)) {
                filePath = filePath.substring(FILE_PREFIX.length());
                inStream = getFileAsStream(filePath);
                xml = filePath.endsWith(XML_SUFFIX);
            } else if (filePath.startsWith(HTTP_PREFIX) || filePath.startsWith(HTTPS_PREFIX)) {
                URL url = new URL(filePath);
                inStream = url.openStream();
                xml = url.getPath().endsWith(XML_SUFFIX);
            } else if (filePath.startsWith(CLASSPATH_PREFIX)) {
                String resourcePath = filePath.substring(CLASSPATH_PREFIX.length());
                inStream = getFileAsStream(resourcePath);
                xml = resourcePath.endsWith(XML_SUFFIX);
            } else {
                inStream = getFileAsStream(filePath);
                xml = filePath.endsWith(XML_SUFFIX);
            }
            if (inStream == null) {
                System.out.println("load config file error, file : " + filePath);
                return null;
            }
            if (xml) {
                properties.loadFromXML(inStream);
            } else {
                properties.load(inStream);
            }
            return properties;
        } catch (Exception ex) {
            System.out.println("load config file error, file : " + filePath);
            return null;
        } finally {
            inStream.close();
        }
    }

    /**
     * 将指定的数据文件读取到InputStream流中
     *
     * @param filePath 文件路径
     * @return InputStream流
     */
    public static InputStream getFileAsStream(String filePath) throws FileNotFoundException {
        InputStream inStream;
        File file = new File(filePath);
        if (file.exists()) {
            inStream = new FileInputStream(file);
        } else {
            inStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        }
        return inStream;
    }
}
