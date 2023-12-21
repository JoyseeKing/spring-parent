package com.emily.infrastructure.core.helper;

import com.emily.infrastructure.core.constant.HeaderInfo;
import com.emily.infrastructure.core.exception.BasicException;
import com.emily.infrastructure.core.exception.HttpStatusType;
import com.emily.infrastructure.core.exception.PrintExceptionInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * HttpServletRequest请求类
 *
 * @author Emily
 */
public class RequestUtils {
    /**
     * unknown
     */
    private static final String UNKNOWN = "unknown";
    /**
     * 本地IP
     */
    private static final String LOCAL_IP = "127.0.0.1";
    /**
     * IP地址0:0:0:0:0:0:0:1是一个IPv6地址，通常用于表示本地回环地址（localhost）
     */
    private static final String LOCAL_HOST = "0:0:0:0:0:0:0:1";
    /**
     * 服务器IP
     */
    private static String SERVER_IP = null;
    /**
     * 是否是内网正则表达式
     */
    private static String INTERNET = "^(127\\.0\\.0\\.1)|(localhost)|(10\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})|(172\\.((1[6-9])|(2\\d)|(3[01]))\\.\\d{1,3}\\.\\d{1,3})|(192\\.168\\.\\d{1,3}\\.\\d{1,3})$";

    /**
     * 获取客户端IP
     *
     * @return 客户端IP
     */
    public static String getClientIp() {
        if (isServlet()) {
            return getClientIp(getRequest());
        }
        return LOCAL_IP;
    }

    /**
     * 获取客户单IP地址
     *
     * @param request 请求对象
     * @return 客户端IP
     */
    public static String getClientIp(HttpServletRequest request) {
        try {
            String ip = request.getHeader(HeaderInfo.X_FORWARDED_FOR);
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader(HeaderInfo.PROXY_CLIENT_IP);
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader(HeaderInfo.WL_PROXY_CLIENT_IP);
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader(HeaderInfo.HTTP_CLIENT_IP);
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getHeader(HeaderInfo.HTTP_X_FORWARDED_FOR);
            }
            if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (StringUtils.equalsIgnoreCase(LOCAL_HOST, ip)) {
                ip = LOCAL_IP;
            }
            return ip;
        } catch (Exception exception) {
            return StringUtils.EMPTY;
        }
    }

    /**
     * 判定是否是内网地址
     *
     * @param ip IP地址
     * @return 是否内网
     */
    public static boolean isInternet(String ip) {
        if (StringUtils.isEmpty(ip)) {
            return false;
        }
        if (StringUtils.equals(LOCAL_HOST, ip)) {
            return true;
        }
        Pattern reg = Pattern.compile(INTERNET);
        Matcher match = reg.matcher(ip);
        return match.find();
    }

    /**
     * 获取服务器端的IP
     *
     * @return 服务器端IP
     */
    public static String getServerIp() {
        if (StringUtils.isNotEmpty(SERVER_IP)) {
            return SERVER_IP;
        }
        try {
            Enumeration<NetworkInterface> allNetInterfaces = NetworkInterface.getNetworkInterfaces();
            while (allNetInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = allNetInterfaces.nextElement();
                String name = netInterface.getName();
                if (!StringUtils.contains(name, "docker") && !StringUtils.contains(name, "lo")) {
                    Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress ip = addresses.nextElement();
                        //loopback地址即本机地址，IPv4的loopback范围是127.0.0.0 ~ 127.255.255.255
                        if (ip != null
                                && !ip.isLoopbackAddress()
                                && !ip.getHostAddress().contains(":")) {
                            SERVER_IP = ip.getHostAddress();
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            SERVER_IP = LOCAL_IP;
        }
        return SERVER_IP;
    }

    /**
     * 是否存在servlet上下文
     *
     * @return 是否servlet上下文
     */
    public static boolean isServlet() {
        return RequestContextHolder.getRequestAttributes() == null ? false : true;
    }

    /**
     * 获取用户当前请求的HttpServletRequest
     *
     * @return 请求对象
     */
    public static HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            return attributes.getRequest();
        } catch (Exception ex) {
            throw new BasicException(HttpStatusType.EXCEPTION.getStatus(), PrintExceptionInfo.printErrorInfo(ex));
        }
    }

    /**
     * 获取当前请求的HttpServletResponse
     *
     * @return 响应对象
     */
    public static HttpServletResponse getResponse() {
        try {
            ServletRequestAttributes attributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
            return attributes.getResponse();
        } catch (Exception ex) {
            throw new BasicException(HttpStatusType.EXCEPTION.getStatus(), PrintExceptionInfo.printErrorInfo(ex));
        }
    }

    public static void setServerIp(String serverIp) {
        SERVER_IP = serverIp;
    }

    /**
     * 获取请求头，请求头必须传
     *
     * @param header 请求头
     * @return 请求头结果
     */
    public static String getHeader(String header) {
        return getHeader(header, false);
    }

    /**
     * 获取请求头
     *
     * @param header   请求头
     * @param required 是否允许请求头为空或者不传递，true-是；false-否
     * @return 请求头结果
     */
    public static String getHeader(String header, boolean required) {
        if (header == null || header.length() == 0) {
            return header;
        }
        String value = getRequest().getHeader(header);
        if (!required) {
            return value;
        }
        if (value == null || value.length() == 0) {
            throw new IllegalArgumentException(HttpStatusType.ILLEGAL_ARGUMENT.getMessage());
        }
        return value;
    }
}
