package org.esb.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class UrlUtil {
    public static String getDirFromClassLoader(Class<?> cls) {
        try {
            String path = cls.getName().replace(".", "/");
            path = "/" + path + ".class";
            URL url = cls.getResource(path);
            String jarUrl = url.getPath();
            jarUrl = URLDecoder.decode(jarUrl, "UTF-8");
            if (jarUrl.startsWith("file:")) {
                if (jarUrl.length() > 5) {
                    jarUrl = jarUrl.substring(5);
                }
                jarUrl = jarUrl.split("!")[0];

            } else {
                jarUrl = cls.getResource("/").toString().substring(5);
            }
            File file = new File(jarUrl);
            return file.getParent();

        } catch (Exception e) {
        }
        return null;
    }

    public static String filePath2Utf8(File file) {
        String path = file.getPath();
        try {
            return URLDecoder.decode(path, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String url2Utf8(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
