package org.esb.test;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ResourceUtils;
import org.esb.util.UrlUtil;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by 枫 on 2014/8/4.
 */
public class TestMain {
    public static void main(String[] args){
        try {
            File f = ResourceUtils.getFile(UrlUtil.url2Utf8(UrlUtil
                    .getDirFromClassLoader(TestMain.class))
                    + File.separatorChar
                    + "conf" + File.separatorChar + "log4j.properties");
            PropertyConfigurator.configure(f.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        String confBase = "file:"
                + UrlUtil.url2Utf8(UrlUtil.getDirFromClassLoader(TestMain.class))
                + File.separatorChar + "conf" + File.separatorChar;

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                confBase + "spring-config.xml");


    }
}
