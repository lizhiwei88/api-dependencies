package com.github.lizhiwei88.dependencies.agent.util;

import org.springframework.util.StringUtils;

/**
 * @author Zhiwei Li
 */
public class Utils {

    private Utils() {
    }

    public static String httpHead(String str) {
        if (!str.startsWith("http://") && !str.startsWith("https://")) {
            return "http://" + str;
        }
        return str;
    }

    public static String getPath(String path) {
        if (StringUtils.hasText(path)) {
            path = path.trim();
            if (!path.startsWith("/")) {
                path = "/" + path;
            }
            if (path.endsWith("/")) {
                path = path.substring(0, path.length() - 1);
            }
        }
        return path;
    }
}
