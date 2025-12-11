package ats.algo.sport.outrights.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
public class OutrightsConfiguration {

    @Autowired
    private Environment env;

    @Value("${urlForMqBroker:tcp://localhost:61616/}")
    private String urlForMqBroker;

    @Value("${persistedDataFolder:c:/outrights/}")
    private String persistedDataFolder;

    @Value("${loadPersistedData:true}")
    private boolean loadPersistedData;

    @Value("${ats.db.username:ats_owner}")
    private String atsDbUsername;

    @Value("${ats.db.password:ats_owner}")
    private String atsDbPassword;

    @Value("${ats.db.url:jdbc:postgresql://enamwuatdbs01:5432/ats_whitelabel?tcpKeepAlive=true&prepareThreshold=0}")
    private String atsDbUrl;

    @Value("outrights.premierleague1819.eventid:5537098")
    private String premierleague1819EventId;

    public String getBrokerUrl() {
        return urlForMqBroker;
    }

    public Environment getEnv() {
        return env;
    }

    public void setAtsDbUsername(String atsDbUsername) {
        this.atsDbUsername = atsDbUsername;
    }

    public void setAtsDbPassword(String atsDbPassword) {
        this.atsDbPassword = atsDbPassword;
    }

    public void setAtsDbUrl(String atsDbUrl) {
        this.atsDbUrl = atsDbUrl;
    }

    public String getPersistedDataFolder() {
        return persistedDataFolder;
    }

    public String getAtsDbUsername() {
        return atsDbUsername;
    }

    public String getAtsDbPassword() {
        return atsDbPassword;
    }

    public String getAtsDbUrl() {
        return atsDbUrl;
    }

    public boolean isLoadPersistedData() {
        return loadPersistedData;
    }

    public String getUrlForMqBroker() {
        return urlForMqBroker;
    }

    public String getPremierleague1819EventId() {
        return premierleague1819EventId;
    }

    @Override
    public String toString() {
        return "OutrightsConfiguration [env=" + env + ", urlForMqBroker=" + urlForMqBroker + ", persistedDataFolder="
                        + persistedDataFolder + ", loadPersistedData=" + loadPersistedData + ", atsDbUsername="
                        + atsDbUsername + ", atsDbPassword=" + atsDbPassword + ", atsDbUrl=" + atsDbUrl
                        + ", premierleague1819EventId=" + premierleague1819EventId + "]";
    }

}
