package org.esb.provider;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.esb.akka.AkkaEsbSystem;
import org.esb.common.Provider;

/**
 * @author Andy.Cao
 * @date 2018-11-21
 * @deprecated
 */
public class ProviderFactoryBean implements ApplicationContextAware {

    private AkkaEsbSystem akkaEsbSystem;

    private Logger logger = Logger.getLogger(ProviderFactoryBean.class);

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {

        Map<String, Object> beans = applicationContext
                .getBeansWithAnnotation(Provider.class);
        for (Iterator<String> iterator = beans.keySet().iterator(); iterator
                .hasNext(); ) {
            String key = iterator.next();
            logger.info("Provider [" + key + "] " + beans.get(key));
            Object bean = beans.get(key);
            Provider anno = bean.getClass().getAnnotation(Provider.class);
            Class<?>[] interfaces = bean.getClass().getInterfaces();

            // 启动所有注册的服务
//			String actorName = key;
//			if (interfaces.length > 0) {
//				actorName = interfaces[0].getName();
//				if (anno == null) {
//					anno = interfaces[0].getAnnotation(Service.class);
//				}
//			} else {
//				actorName = bean.getClass().getName();
//			}
            ProviderBean provider = new ProviderBean(interfaces[0], anno.protocol(),
                    bean);
            akkaEsbSystem.getProtocolFactory().provide(provider);
        }
        akkaEsbSystem.getProtocolFactory().start();
    }

    public AkkaEsbSystem getAkkaEsbSystem() {
        return akkaEsbSystem;
    }

    public void setAkkaEsbSystem(AkkaEsbSystem akkaEsbSystem) {
        this.akkaEsbSystem = akkaEsbSystem;
    }

}
