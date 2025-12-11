package ats.algo.betstars;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author Salvatore Rapisarda on 03/08/2016.
 * @link com.betstars.algo.ats.integration.config
 */
@Configuration
@ComponentScan(basePackages = "com.betstars.algo")
public class BetstarsContextConfig {
    @Bean(name = "tennisMatchFormat")
    public ats.algo.sport.tennis.TennisMatchFormat tennisMatchFormat() {
        return new ats.algo.sport.tennis.TennisMatchFormat();
    }

    @Bean(name = "tennisEngine")
    public com.betstars.algo.ats.integration.BsTennisMatchEngine tennisEngine() {
        return new com.betstars.algo.ats.integration.BsTennisMatchEngine(tennisMatchFormat());
    }

    @Bean(name = "volleyballMatchFormat")
    public ats.algo.sport.volleyball.VolleyballMatchFormat volleyballMatchFormat() {
        return new ats.algo.sport.volleyball.VolleyballMatchFormat();
    }

    // @Bean(name = "volleyballEngine")
    // public com.betstars.algo.ats.integration.BsVolleyballMatchEngine volleyballEngine() {
    // return new com.betstars.algo.ats.integration.BsVolleyballMatchEngine(volleyballMatchFormat());
    // }

}
