package ats.algo.springbridge;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Provides access to the spring application context when the Algo Manager is bootstrapped in spring.
 */
public class SpringContextBridge implements ApplicationContextAware {
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        newApplicationContext(applicationContext);
    }

    public static void newApplicationContext(ApplicationContext applicationContext) {
        SpringContextBridge.applicationContext = applicationContext;
    }

    /**
     * Returns the spring application context
     * 
     * @return
     */
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

}
