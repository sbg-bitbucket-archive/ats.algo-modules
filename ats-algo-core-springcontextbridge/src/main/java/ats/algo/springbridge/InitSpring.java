package ats.algo.springbridge;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import ats.core.util.log.Logger;
import ats.core.util.log.LoggerFactory;

public class InitSpring {
    private static final Logger log = LoggerFactory.getLogger(GetInstancesFromProperties.class);

    public static void init() {
        String componentScanPackage = System.getProperty("algomgr.springBridgeComponentScanPackage");
        log.info("algomgr.springBridgeComponentScanPackage = " + componentScanPackage);
        if (componentScanPackage != null) {
            log.info("initialising spring context...");
            String[] packages = componentScanPackage.split(",");
            AnnotationConfigApplicationContext springApplicationContext =
                            new AnnotationConfigApplicationContext(packages);
            SpringContextBridge.newApplicationContext(springApplicationContext);
        }
    }
}
